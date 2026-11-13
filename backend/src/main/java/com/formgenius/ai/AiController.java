package com.formgenius.ai;

import com.formgenius.ai.dto.*;
import com.formgenius.common.ApiResponse;
import com.formgenius.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/ai", "/api/v1/ai"})
@Tag(name = "AI Generation", description = "Endpoints for Google Gemini AI form generation, question optimization, translation, and sentiment insights")
public class AiController {

    private final GeminiAiService aiService;

    @PostMapping({"/generate-form", "/forms/generate"})
    @Operation(summary = "Generate a full form schema from natural language prompt using Google Gemini")
    public ResponseEntity<ApiResponse<AiFormSchemaResponse>> generateForm(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AiGenerateFormRequest request) {
        AiFormSchemaResponse response = aiService.generateForm(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Form generated successfully by AI", response));
    }

    @PostMapping({"/improve-question", "/questions/improve"})
    @Operation(summary = "Improve wording and options of a question with follow-up suggestions")
    public ResponseEntity<ApiResponse<Map<String, String>>> improveQuestion(
            @Valid @RequestBody AiImproveQuestionRequest request) {
        Map<String, String> response = aiService.improveQuestion(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/generate-validation")
    @Operation(summary = "Generate validation rules and regex for a question")
    public ResponseEntity<ApiResponse<AiValidationSuggestionResponse>> generateValidation(
            @RequestBody AiValidationSuggestionRequest request) {
        AiValidationSuggestionResponse response = aiService.generateValidation(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping({"/summarize-form", "/analyze-form"})
    @Operation(summary = "Analyze form structure, completion estimate, and response rate recommendations")
    public ResponseEntity<ApiResponse<AiFormAnalysisResponse>> summarizeForm(
            @RequestBody AiFormAnalysisRequest request) {
        AiFormAnalysisResponse response = aiService.summarizeForm(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/suggest-questions")
    @Operation(summary = "Suggest additional questions based on existing questions")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> suggestQuestions(
            @RequestBody AiSuggestQuestionsRequest request) {
        List<Map<String, String>> response = aiService.suggestQuestions(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping({"/forms/translate", "/translate"})
    @Operation(summary = "Translate an entire form into another language")
    public ResponseEntity<ApiResponse<AiFormSchemaResponse>> translateForm(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AiTranslateFormRequest request) {
        AiFormSchemaResponse response = aiService.translateForm(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Form translated successfully", response));
    }

    @PostMapping({"/insights/generate", "/insights"})
    @Operation(summary = "Generate AI sentiment analysis, common complaint synthesis, and recommendations from response data")
    public ResponseEntity<ApiResponse<AiInsightsResponse>> generateInsights(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody AiGenerateInsightsRequest request) {
        AiInsightsResponse response = aiService.generateInsights(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    public AiController(GeminiAiService aiService) {
        this.aiService = aiService;
    }
}
