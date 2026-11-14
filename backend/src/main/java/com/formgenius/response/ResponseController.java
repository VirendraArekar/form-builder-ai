package com.formgenius.response;

import com.formgenius.common.ApiResponse;
import com.formgenius.common.PageResponse;
import com.formgenius.response.dto.*;
import com.formgenius.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping({"/api/forms/{formId}/responses", "/api/v1/forms/{formId}/responses"})
@Tag(name = "Form Responses", description = "Endpoints for viewing, tagging, annotating, and exporting form submissions")
public class ResponseController {

    private final ResponseService responseService;

    @GetMapping
    @Operation(summary = "List all submissions for a form with pagination and filtering")
    public ResponseEntity<ApiResponse<PageResponse<ResponseSummaryDto>>> listResponses(
            @PathVariable UUID formId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean isReviewed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "submittedAt,desc") String sort,
            @AuthenticationPrincipal UserPrincipal principal) {

        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<ResponseSummaryDto> result = responseService.listResponses(formId, principal.getId(), status, isReviewed, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(result)));
    }

    @GetMapping("/{responseId}")
    @Operation(summary = "Get full response details by ID")
    public ResponseEntity<ApiResponse<ResponseDetailDto>> getResponseDetails(
            @PathVariable UUID formId,
            @PathVariable UUID responseId,
            @AuthenticationPrincipal UserPrincipal principal) {
        ResponseDetailDto detail = responseService.getResponseDetails(formId, responseId, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(detail));
    }

    @DeleteMapping("/{responseId}")
    @Operation(summary = "Delete an individual submission")
    public ResponseEntity<ApiResponse<Void>> deleteResponse(
            @PathVariable UUID formId,
            @PathVariable UUID responseId,
            @AuthenticationPrincipal UserPrincipal principal) {
        responseService.deleteResponse(formId, responseId, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Response deleted successfully", null));
    }

    @PostMapping("/bulk-delete")
    @Operation(summary = "Bulk delete submissions")
    public ResponseEntity<ApiResponse<Void>> bulkDeleteResponses(
            @PathVariable UUID formId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody BulkDeleteRequest request) {
        responseService.bulkDeleteResponses(formId, principal.getId(), request.getResponseIds());
        return ResponseEntity.ok(ApiResponse.success("Selected responses deleted successfully", null));
    }

    @PatchMapping("/{responseId}/review")
    @Operation(summary = "Toggle review status for submission")
    public ResponseEntity<ApiResponse<Void>> toggleReview(
            @PathVariable UUID formId,
            @PathVariable UUID responseId,
            @AuthenticationPrincipal UserPrincipal principal) {
        responseService.toggleReviewStatus(formId, responseId, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Review status updated", null));
    }

    @PostMapping("/{responseId}/notes")
    @Operation(summary = "Add an internal note to a submission")
    public ResponseEntity<ApiResponse<ResponseNoteDto>> addNote(
            @PathVariable UUID formId,
            @PathVariable UUID responseId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AddNoteRequest request) {
        ResponseNoteDto note = responseService.addNote(formId, responseId, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Note added successfully", note));
    }

    @PostMapping("/{responseId}/tags")
    @Operation(summary = "Add a tag to a submission")
    public ResponseEntity<ApiResponse<ResponseTagDto>> addTag(
            @PathVariable UUID formId,
            @PathVariable UUID responseId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AddTagRequest request) {
        ResponseTagDto tag = responseService.addTag(formId, responseId, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Tag added successfully", tag));
    }

    @DeleteMapping("/{responseId}/tags/{tagId}")
    @Operation(summary = "Remove a tag from a submission")
    public ResponseEntity<ApiResponse<Void>> removeTag(
            @PathVariable UUID formId,
            @PathVariable UUID responseId,
            @PathVariable UUID tagId,
            @AuthenticationPrincipal UserPrincipal principal) {
        responseService.removeTag(formId, responseId, tagId, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Tag removed successfully", null));
    }

    @GetMapping(value = {"/export", "/export/csv"}, produces = "text/csv")
    @Operation(summary = "Export all form submissions to CSV format")
    public ResponseEntity<String> exportCsv(
            @PathVariable UUID formId,
            @AuthenticationPrincipal UserPrincipal principal) {
        String csvData = responseService.exportResponsesCsv(formId, principal.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "form-submissions-" + formId + ".csv");

        return ResponseEntity.ok().headers(headers).body(csvData);
    }

    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }
}
