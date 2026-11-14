package com.formgenius.analytics;

import com.formgenius.analytics.dto.FormAnalyticsOverviewDto;
import com.formgenius.analytics.dto.QuestionAnalyticsDto;
import com.formgenius.analytics.dto.WorkspaceAnalyticsDto;
import com.formgenius.common.ApiResponse;
import com.formgenius.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/api", "/api/v1"})
@Tag(name = "Analytics", description = "Endpoints for PostgreSQL-powered real-time form and workspace analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping({"/forms/{id}/analytics", "/forms/{id}/analytics/overview"})
    @Operation(summary = "Get form-level analytics overview (views, submissions, completion rate, device distribution, daily trends)")
    public ResponseEntity<ApiResponse<FormAnalyticsOverviewDto>> getFormOverview(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "30") int days,
            @AuthenticationPrincipal UserPrincipal principal) {
        FormAnalyticsOverviewDto overview = analyticsService.getFormOverview(id, principal.getId(), days);
        return ResponseEntity.ok(ApiResponse.success(overview));
    }

    @GetMapping("/forms/{id}/analytics/questions")
    @Operation(summary = "Get per-question breakdown metrics and charts data")
    public ResponseEntity<ApiResponse<List<QuestionAnalyticsDto>>> getQuestionAnalytics(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        List<QuestionAnalyticsDto> list = analyticsService.getQuestionAnalytics(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/workspaces/{id}/analytics")
    @Operation(summary = "Get workspace-wide analytics and performance comparison")
    public ResponseEntity<ApiResponse<WorkspaceAnalyticsDto>> getWorkspaceAnalytics(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "30") int days,
            @AuthenticationPrincipal UserPrincipal principal) {
        WorkspaceAnalyticsDto wsAnalytics = analyticsService.getWorkspaceAnalytics(id, principal.getId(), days);
        return ResponseEntity.ok(ApiResponse.success(wsAnalytics));
    }

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }
}
