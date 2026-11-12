package com.formgenius.form;

import com.formgenius.common.ApiResponse;
import com.formgenius.common.PageResponse;
import com.formgenius.exception.BadRequestException;
import com.formgenius.form.dto.*;
import com.formgenius.security.UserPrincipal;
import com.formgenius.workspace.WorkspaceService;
import com.formgenius.workspace.dto.WorkspaceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/api/forms", "/api/v1/forms"})
@Tag(name = "Forms", description = "Endpoints for managing forms, drag-and-drop schema persistence, and publishing")
public class FormController {

    private final FormService formService;
    private final WorkspaceService workspaceService;

    @GetMapping
    @Operation(summary = "List forms with status, favorite, & search filter")
    public ResponseEntity<ApiResponse<PageResponse<FormSummaryResponse>>> listForms(
            @RequestParam(required = false) UUID workspaceId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean favorite,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @AuthenticationPrincipal UserPrincipal principal) {

        UUID targetWorkspaceId = workspaceId;
        if (targetWorkspaceId == null) {
            List<WorkspaceResponse> wsList = workspaceService.listUserWorkspaces(principal.getId());
            if (!wsList.isEmpty()) {
                targetWorkspaceId = wsList.get(0).getId();
            } else {
                throw new BadRequestException("No active workspace found for user.");
            }
        }

        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<FormSummaryResponse> result = formService.listForms(targetWorkspaceId, principal.getId(), status, favorite, search, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(result)));
    }

    @PostMapping
    @Operation(summary = "Create a new form")
    public ResponseEntity<ApiResponse<FormDetailResponse>> createForm(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateFormRequest request) {
        if (request.getWorkspaceId() == null) {
            List<WorkspaceResponse> wsList = workspaceService.listUserWorkspaces(principal.getId());
            if (!wsList.isEmpty()) {
                request.setWorkspaceId(wsList.get(0).getId());
            }
        }
        FormDetailResponse form = formService.createForm(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Form created successfully", form));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get full form details with sections, questions, logic rules, and theme")
    public ResponseEntity<ApiResponse<FormDetailResponse>> getForm(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        FormDetailResponse form = formService.getFormDetails(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(form));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update form settings or complete schema")
    public ResponseEntity<ApiResponse<FormDetailResponse>> updateForm(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody SaveFormSchemaRequest request) {
        FormDetailResponse form = formService.saveFormSchema(id, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Form updated successfully", form));
    }

    @PutMapping("/{id}/schema")
    @Operation(summary = "Save complete form schema (sections, questions, logic rules, theme)")
    public ResponseEntity<ApiResponse<FormDetailResponse>> saveFormSchema(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody SaveFormSchemaRequest request) {
        FormDetailResponse form = formService.saveFormSchema(id, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Form schema saved successfully", form));
    }

    @PostMapping("/{id}/duplicate")
    @Operation(summary = "Duplicate an existing form")
    public ResponseEntity<ApiResponse<FormDetailResponse>> duplicateForm(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        FormDetailResponse duplicate = formService.duplicateForm(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Form duplicated successfully", duplicate));
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish form with public URL slug")
    public ResponseEntity<ApiResponse<FormDetailResponse>> publishForm(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody(required = false) PublishFormRequest request) {
        FormDetailResponse form = formService.publishForm(id, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Form published successfully", form));
    }

    @PostMapping("/{id}/unpublish")
    @Operation(summary = "Unpublish form")
    public ResponseEntity<ApiResponse<FormDetailResponse>> unpublishForm(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        FormDetailResponse form = formService.unpublishForm(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Form unpublished successfully", form));
    }

    @PatchMapping("/{id}/favorite")
    @Operation(summary = "Toggle favorite status of a form")
    public ResponseEntity<ApiResponse<FormDetailResponse>> toggleFavorite(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        FormDetailResponse form = formService.toggleFavorite(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Form favorite status updated", form));
    }

    @PatchMapping("/{id}/archive")
    @Operation(summary = "Toggle archive status of a form")
    public ResponseEntity<ApiResponse<FormDetailResponse>> toggleArchive(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        FormDetailResponse form = formService.toggleArchive(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Form archive status updated", form));
    }

    @PostMapping("/{id}/archive")
    @Operation(summary = "Archive form")
    public ResponseEntity<ApiResponse<Void>> archiveForm(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        formService.archiveForm(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Form archived", null));
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore archived form")
    public ResponseEntity<ApiResponse<Void>> restoreForm(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        formService.restoreForm(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Form restored to draft", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete form")
    public ResponseEntity<ApiResponse<Void>> deleteForm(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        formService.deleteForm(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Form deleted successfully", null));
    }

    public FormController(FormService formService, WorkspaceService workspaceService) {
        this.formService = formService;
        this.workspaceService = workspaceService;
    }
}
