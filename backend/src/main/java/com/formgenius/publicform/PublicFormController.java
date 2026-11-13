package com.formgenius.publicform;

import com.formgenius.common.ApiResponse;
import com.formgenius.publicform.dto.PublicFormViewResponse;
import com.formgenius.publicform.dto.PublicSubmissionResultResponse;
import com.formgenius.publicform.dto.SubmitPublicFormRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/public/forms", "/api/v1/public/forms"})
@Tag(name = "Public Form Submissions", description = "Public facing endpoints for respondent viewing and form submission")
public class PublicFormController {

    private final PublicFormService publicFormService;

    @GetMapping("/{slug}")
    @Operation(summary = "Get published form by URL slug for public rendering")
    public ResponseEntity<ApiResponse<PublicFormViewResponse>> getPublicForm(
            @PathVariable String slug,
            @RequestParam(required = false) String password,
            @RequestHeader(value = "X-Form-Password", required = false) String headerPassword,
            HttpServletRequest request) {

        String authPass = password != null ? password : headerPassword;
        PublicFormViewResponse form = publicFormService.getPublicForm(slug, authPass, request);
        return ResponseEntity.ok(ApiResponse.success(form));
    }

    @PostMapping({"/{slug}/respond", "/{slug}/submissions", "/{slug}/submit"})
    @Operation(summary = "Submit answers for a published form with full server-side validation")
    public ResponseEntity<ApiResponse<PublicSubmissionResultResponse>> submitForm(
            @PathVariable String slug,
            @RequestBody SubmitPublicFormRequest request,
            HttpServletRequest servletRequest) {

        PublicSubmissionResultResponse result = publicFormService.submitPublicForm(slug, request, servletRequest);
        return ResponseEntity.ok(ApiResponse.success("Response submitted successfully", result));
    }

    public PublicFormController(PublicFormService publicFormService) {
        this.publicFormService = publicFormService;
    }
}
