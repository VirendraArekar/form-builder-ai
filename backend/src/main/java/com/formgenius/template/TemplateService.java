package com.formgenius.template;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formgenius.exception.ResourceNotFoundException;
import com.formgenius.form.FormService;
import com.formgenius.form.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final FormService formService;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<TemplateDto> listTemplates(String category, String search) {
        String filterCat = (category == null || category.equalsIgnoreCase("All") || category.trim().isEmpty())
                ? null
                : category.trim().toLowerCase();
        String searchPattern = (search == null || search.trim().isEmpty())
                ? null
                : "%" + search.trim().toLowerCase() + "%";

        List<FormTemplate> list = templateRepository.findFiltered(filterCat, searchPattern);
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TemplateDto getTemplate(UUID id) {
        FormTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
        return mapToDto(template);
    }

    @Transactional
    public TemplateDto createTemplate(CreateTemplateRequest request) {
        FormTemplate template = FormTemplate.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory() != null ? request.getCategory() : "Custom")
                .icon(request.getIcon() != null ? request.getIcon() : "FileText")
                .isFeatured(false)
                .usageCount(0)
                .templateSchema(request.getTemplateSchema() != null ? request.getTemplateSchema() : "{}")
                .build();
        template = templateRepository.save(template);
        return mapToDto(template);
    }

    @Transactional
    public FormDetailResponse useTemplate(UUID templateId, UUID workspaceId, UUID userId) {
        FormTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        template.setUsageCount(template.getUsageCount() + 1);
        templateRepository.save(template);

        CreateFormRequest createReq = new CreateFormRequest();
        createReq.setWorkspaceId(workspaceId);
        createReq.setTitle(template.getTitle());
        createReq.setDescription(template.getDescription());

        FormDetailResponse newForm = formService.createForm(userId, createReq);

        // Parse template schema and populate into form
        try {
            JsonNode root = objectMapper.readTree(template.getTemplateSchema());
            List<FormSectionDto> sections = new ArrayList<>();
            List<FormQuestionDto> questions = new ArrayList<>();

            if (root.has("sections") && root.get("sections").isArray()) {
                int qIdx = 0;
                for (int sIdx = 0; sIdx < root.get("sections").size(); sIdx++) {
                    JsonNode sNode = root.get("sections").get(sIdx);
                    UUID secId = UUID.randomUUID();
                    sections.add(FormSectionDto.builder()
                            .id(secId)
                            .title(sNode.path("title").asText("Section " + (sIdx + 1)))
                            .orderIndex(sIdx)
                            .build());

                    if (sNode.has("questions") && sNode.get("questions").isArray()) {
                        for (JsonNode qNode : sNode.get("questions")) {
                            List<FormOptionDto> options = new ArrayList<>();
                            if (qNode.has("options") && qNode.get("options").isArray()) {
                                for (int oIdx = 0; oIdx < qNode.get("options").size(); oIdx++) {
                                    String optVal = qNode.get("options").get(oIdx).asText();
                                    options.add(FormOptionDto.builder()
                                            .label(optVal)
                                            .value(optVal.toLowerCase().replaceAll("[^a-z0-9]", "_"))
                                            .orderIndex(oIdx)
                                            .build());
                                }
                            }

                            questions.add(FormQuestionDto.builder()
                                    .id(UUID.randomUUID())
                                    .sectionId(secId)
                                    .fieldKey(qNode.path("key").asText("field_" + qIdx))
                                    .title(qNode.path("title").asText("Question"))
                                    .questionType(qNode.path("type").asText("SHORT_TEXT"))
                                    .isRequired(qNode.path("required").asBoolean(false))
                                    .orderIndex(qIdx++)
                                    .options(options)
                                    .build());
                        }
                    }
                }
            }

            SaveFormSchemaRequest schemaReq = new SaveFormSchemaRequest();
            schemaReq.setTitle(newForm.getTitle());
            schemaReq.setDescription(newForm.getDescription());
            schemaReq.setSections(sections);
            schemaReq.setQuestions(questions);
            schemaReq.setLogicRules(new ArrayList<>());
            schemaReq.setTheme(newForm.getTheme());

            return formService.saveFormSchema(newForm.getId(), userId, schemaReq);

        } catch (Exception e) {
            log.error("Failed to parse template schema: {}", e.getMessage());
            return newForm;
        }
    }

    private TemplateDto mapToDto(FormTemplate t) {
        return TemplateDto.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .category(t.getCategory())
                .icon(t.getIcon())
                .templateSchema(t.getTemplateSchema())
                .isFeatured(t.isFeatured())
                .usageCount(t.getUsageCount())
                .build();
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TemplateService.class);

    public TemplateService(TemplateRepository templateRepository, FormService formService, ObjectMapper objectMapper) {
        this.templateRepository = templateRepository;
        this.formService = formService;
        this.objectMapper = objectMapper;
    }
}
