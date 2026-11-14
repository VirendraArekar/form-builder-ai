package com.formgenius.template;

import com.formgenius.common.ApiResponse;
import com.formgenius.form.dto.FormDetailResponse;
import com.formgenius.security.UserPrincipal;
import com.formgenius.workspace.WorkspaceService;
import com.formgenius.workspace.dto.WorkspaceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/api/templates", "/api/v1/templates"})
@Tag(name = "Templates", description = "Endpoints for exploring pre-built industry form templates and cloning them into workspaces")
public class TemplateController {

    private final TemplateService templateService;
    private final WorkspaceService workspaceService;

    @GetMapping
    @Operation(summary = "List all available templates with category and keyword filter")
    public ResponseEntity<ApiResponse<List<TemplateDto>>> listTemplates(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {
        List<TemplateDto> list = templateService.listTemplates(category, search);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get template details by ID")
    public ResponseEntity<ApiResponse<TemplateDto>> getTemplate(@PathVariable UUID id) {
        TemplateDto template = templateService.getTemplate(id);
        return ResponseEntity.ok(ApiResponse.success(template));
    }

    @PostMapping
    @Operation(summary = "Create custom form template")
    public ResponseEntity<ApiResponse<TemplateDto>> createTemplate(
            @Valid @RequestBody CreateTemplateRequest request) {
        TemplateDto template = templateService.createTemplate(request);
        return ResponseEntity.ok(ApiResponse.success("Template created successfully", template));
    }

    @PostMapping("/{id}/use")
    @Operation(summary = "Instantiate a new form in workspace from template")
    public ResponseEntity<ApiResponse<FormDetailResponse>> useTemplate(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID workspaceId,
            @AuthenticationPrincipal UserPrincipal principal) {
        UUID targetWs = workspaceId;
        if (targetWs == null) {
            List<WorkspaceResponse> wsList = workspaceService.listUserWorkspaces(principal.getId());
            if (!wsList.isEmpty()) {
                targetWs = wsList.get(0).getId();
            }
        }
        FormDetailResponse form = templateService.useTemplate(id, targetWs, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Form created from template", form));
    }

    public TemplateController(TemplateService templateService, WorkspaceService workspaceService) {
        this.templateService = templateService;
        this.workspaceService = workspaceService;
    }
}
