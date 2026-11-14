package com.formgenius.insights;

import com.formgenius.common.ApiResponse;
import com.formgenius.insights.dto.WorkspaceInsightsDetailDto;
import com.formgenius.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/insights", "/api/v1/insights"})
@Tag(name = "Workspace Insights", description = "Global cross-form aggregated insights, charts, and top-performing form leaderboard")
public class InsightsController {

    private final InsightsService insightsService;

    @GetMapping
    @Operation(summary = "Get aggregated workspace insights across all user forms")
    public ResponseEntity<ApiResponse<WorkspaceInsightsDetailDto>> getInsights(@AuthenticationPrincipal UserPrincipal principal) {
        WorkspaceInsightsDetailDto insights = insightsService.getGlobalInsights(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(insights));
    }

    public InsightsController(InsightsService insightsService) {
        this.insightsService = insightsService;
    }
}
