package com.formgenius.response;

import com.formgenius.exception.ResourceNotFoundException;
import com.formgenius.form.Form;
import com.formgenius.form.FormQuestion;
import com.formgenius.form.FormQuestionRepository;
import com.formgenius.form.FormRepository;
import com.formgenius.response.dto.*;
import com.formgenius.user.User;
import com.formgenius.user.UserRepository;
import com.formgenius.workspace.WorkspaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResponseService {

    private final FormResponseRepository responseRepository;
    private final ResponseAnswerRepository answerRepository;
    private final ResponseNoteRepository noteRepository;
    private final ResponseTagRepository tagRepository;
    private final FormRepository formRepository;
    private final FormQuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final WorkspaceService workspaceService;

    @Transactional(readOnly = true)
    public Page<ResponseSummaryDto> listResponses(UUID formId, UUID userId, String status, Boolean isReviewed, Pageable pageable) {
        Form form = validateFormAccess(formId, userId);
        String filterStatus = (status == null || status.equalsIgnoreCase("ALL")) ? null : status.toUpperCase();

        Page<FormResponse> responses = responseRepository.findByFormIdWithFilters(form.getId(), filterStatus, isReviewed, pageable);

        return responses.map(r -> {
            List<ResponseTag> tags = tagRepository.findByResponseId(r.getId());
            List<ResponseNote> notes = noteRepository.findByResponseIdOrderByCreatedAtAsc(r.getId());
            List<ResponseAnswer> answers = answerRepository.findByResponseId(r.getId());

            Map<String, String> answerMap = new HashMap<>();
            for (ResponseAnswer a : answers) {
                answerMap.put(a.getQuestionKey(), a.getAnswerText());
            }

            List<ResponseTagDto> tagDtos = tags.stream().map(t -> ResponseTagDto.builder()
                    .id(t.getId())
                    .tagName(t.getTagName())
                    .color(t.getColor())
                    .createdAt(t.getCreatedAt())
                    .build()).collect(Collectors.toList());

            return ResponseSummaryDto.builder()
                    .id(r.getId())
                    .formId(form.getId())
                    .respondentIp(r.getRespondentIp())
                    .userAgent(r.getUserAgent())
                    .durationSeconds(r.getDurationSeconds())
                    .status(r.getStatus())
                    .isReviewed(r.isReviewed())
                    .submittedAt(r.getSubmittedAt())
                    .notesCount(notes.size())
                    .tags(tagDtos)
                    .answers(answerMap)
                    .build();
        });
    }

    @Transactional(readOnly = true)
    public ResponseDetailDto getResponseDetails(UUID formId, UUID responseId, UUID userId) {
        Form form = validateFormAccess(formId, userId);
        FormResponse response = responseRepository.findById(responseId)
                .orElseThrow(() -> new ResourceNotFoundException("Response not found"));

        if (!response.getForm().getId().equals(form.getId())) {
            throw new ResourceNotFoundException("Response does not belong to form");
        }

        List<ResponseAnswer> answers = answerRepository.findByResponseId(response.getId());
        List<ResponseNote> notes = noteRepository.findByResponseIdOrderByCreatedAtAsc(response.getId());
        List<ResponseTag> tags = tagRepository.findByResponseId(response.getId());

        List<ResponseAnswerDto> answerDtos = answers.stream().map(a -> ResponseAnswerDto.builder()
                .id(a.getId())
                .questionId(a.getQuestion().getId())
                .questionKey(a.getQuestionKey())
                .questionTitle(a.getQuestion().getTitle())
                .questionType(a.getQuestion().getQuestionType())
                .answerText(a.getAnswerText())
                .answerJson(a.getAnswerJson())
                .build()).collect(Collectors.toList());

        List<ResponseNoteDto> noteDtos = notes.stream().map(n -> ResponseNoteDto.builder()
                .id(n.getId())
                .authorId(n.getAuthor().getId())
                .authorName(n.getAuthor().getFirstName() + " " + n.getAuthor().getLastName())
                .noteText(n.getNoteText())
                .createdAt(n.getCreatedAt())
                .build()).collect(Collectors.toList());

        List<ResponseTagDto> tagDtos = tags.stream().map(t -> ResponseTagDto.builder()
                .id(t.getId())
                .tagName(t.getTagName())
                .color(t.getColor())
                .createdAt(t.getCreatedAt())
                .build()).collect(Collectors.toList());

        return ResponseDetailDto.builder()
                .id(response.getId())
                .formId(form.getId())
                .formTitle(form.getTitle())
                .respondentIp(response.getRespondentIp())
                .userAgent(response.getUserAgent())
                .referrer(response.getReferrer())
                .durationSeconds(response.getDurationSeconds())
                .status(response.getStatus())
                .isReviewed(response.isReviewed())
                .submittedAt(response.getSubmittedAt())
                .answers(answerDtos)
                .notes(noteDtos)
                .tags(tagDtos)
                .build();
    }

    @Transactional
    public void deleteResponse(UUID formId, UUID responseId, UUID userId) {
        Form form = validateFormAccess(formId, userId);
        FormResponse response = responseRepository.findById(responseId)
                .orElseThrow(() -> new ResourceNotFoundException("Response not found"));

        if (!response.getForm().getId().equals(form.getId())) {
            throw new ResourceNotFoundException("Response does not belong to this form");
        }

        responseRepository.delete(response);
        if (form.getSubmissionCount() > 0) {
            form.setSubmissionCount(form.getSubmissionCount() - 1);
            formRepository.save(form);
        }
    }

    @Transactional
    public void bulkDeleteResponses(UUID formId, UUID userId, List<UUID> responseIds) {
        Form form = validateFormAccess(formId, userId);
        int deletedCount = 0;
        for (UUID id : responseIds) {
            Optional<FormResponse> opt = responseRepository.findById(id);
            if (opt.isPresent() && opt.get().getForm().getId().equals(form.getId())) {
                responseRepository.delete(opt.get());
                deletedCount++;
            }
        }

        int newCount = Math.max(0, form.getSubmissionCount() - deletedCount);
        form.setSubmissionCount(newCount);
        formRepository.save(form);
    }

    @Transactional
    public void toggleReviewStatus(UUID formId, UUID responseId, UUID userId) {
        Form form = validateFormAccess(formId, userId);
        FormResponse response = responseRepository.findById(responseId)
                .orElseThrow(() -> new ResourceNotFoundException("Response not found"));
        response.setReviewed(!response.isReviewed());
        responseRepository.save(response);
    }

    @Transactional
    public ResponseNoteDto addNote(UUID formId, UUID responseId, UUID userId, AddNoteRequest request) {
        Form form = validateFormAccess(formId, userId);
        FormResponse response = responseRepository.findById(responseId)
                .orElseThrow(() -> new ResourceNotFoundException("Response not found"));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ResponseNote note = ResponseNote.builder()
                .response(response)
                .author(author)
                .noteText(request.getNoteText())
                .build();
        note = noteRepository.save(note);

        return ResponseNoteDto.builder()
                .id(note.getId())
                .authorId(author.getId())
                .authorName(author.getFirstName() + " " + author.getLastName())
                .noteText(note.getNoteText())
                .createdAt(note.getCreatedAt())
                .build();
    }

    @Transactional
    public ResponseTagDto addTag(UUID formId, UUID responseId, UUID userId, AddTagRequest request) {
        Form form = validateFormAccess(formId, userId);
        FormResponse response = responseRepository.findById(responseId)
                .orElseThrow(() -> new ResourceNotFoundException("Response not found"));

        ResponseTag tag = ResponseTag.builder()
                .response(response)
                .tagName(request.getTagName().trim())
                .color(request.getColor() != null ? request.getColor() : "#6366F1")
                .build();
        tag = tagRepository.save(tag);

        return ResponseTagDto.builder()
                .id(tag.getId())
                .tagName(tag.getTagName())
                .color(tag.getColor())
                .createdAt(tag.getCreatedAt())
                .build();
    }

    @Transactional
    public void removeTag(UUID formId, UUID responseId, UUID tagId, UUID userId) {
        validateFormAccess(formId, userId);
        tagRepository.deleteById(tagId);
    }

    @Transactional(readOnly = true)
    public String exportResponsesCsv(UUID formId, UUID userId) {
        Form form = validateFormAccess(formId, userId);
        List<FormQuestion> questions = questionRepository.findByFormIdOrderByOrderIndexAsc(form.getId());
        List<FormResponse> responses = responseRepository.findByFormIdOrderBySubmittedAtDesc(form.getId());

        StringBuilder csv = new StringBuilder();

        // Header row
        csv.append("\"Submission ID\",\"Submitted At (UTC)\",\"Duration (s)\",\"Status\",\"Reviewed\"");
        for (FormQuestion q : questions) {
            csv.append(",\"").append(escapeCsv(q.getTitle())).append("\"");
        }
        csv.append("\n");

        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

        for (FormResponse r : responses) {
            csv.append("\"").append(r.getId()).append("\",");
            csv.append("\"").append(formatter.format(r.getSubmittedAt())).append("\",");
            csv.append(r.getDurationSeconds()).append(",");
            csv.append("\"").append(r.getStatus()).append("\",");
            csv.append(r.isReviewed() ? "Yes" : "No");

            List<ResponseAnswer> answers = answerRepository.findByResponseId(r.getId());
            Map<UUID, String> ansByQ = answers.stream()
                    .collect(Collectors.toMap(a -> a.getQuestion().getId(), a -> a.getAnswerText() != null ? a.getAnswerText() : "", (k1, k2) -> k1));

            for (FormQuestion q : questions) {
                String val = ansByQ.getOrDefault(q.getId(), "");
                csv.append(",\"").append(escapeCsv(val)).append("\"");
            }
            csv.append("\n");
        }

        return csv.toString();
    }

    private Form validateFormAccess(UUID formId, UUID userId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found"));
        workspaceService.findAndValidateMember(form.getWorkspace().getId(), userId);
        return form;
    }

    private String escapeCsv(String str) {
        if (str == null) return "";
        return str.replace("\"", "\"\"");
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ResponseService.class);

    public ResponseService(FormResponseRepository responseRepository, ResponseAnswerRepository answerRepository, ResponseNoteRepository noteRepository, ResponseTagRepository tagRepository, FormRepository formRepository, FormQuestionRepository questionRepository, UserRepository userRepository, WorkspaceService workspaceService) {
        this.responseRepository = responseRepository;
        this.answerRepository = answerRepository;
        this.noteRepository = noteRepository;
        this.tagRepository = tagRepository;
        this.formRepository = formRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.workspaceService = workspaceService;
    }
}
