package com.formgenius.webhook;

import com.formgenius.common.ApiResponse;
import com.formgenius.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workspaces/{workspaceId}/webhooks")
@Tag(name = "Webhooks", description = "Endpoints for configuring webhooks and delivery notifications")
public class WebhookController {

    private final WebhookService webhookService;

    @GetMapping
    @Operation(summary = "List all webhooks configured for a workspace")
    public ResponseEntity<ApiResponse<List<WebhookDto>>> listWebhooks(
            @PathVariable UUID workspaceId,
            @AuthenticationPrincipal UserPrincipal principal) {
        List<WebhookDto> list = webhookService.listWebhooks(workspaceId, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @PostMapping
    @Operation(summary = "Create a new webhook")
    public ResponseEntity<ApiResponse<WebhookDto>> createWebhook(
            @PathVariable UUID workspaceId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateWebhookRequest request) {
        WebhookDto dto = webhookService.createWebhook(workspaceId, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Webhook configured successfully", dto));
    }

    @DeleteMapping("/{webhookId}")
    @Operation(summary = "Delete a webhook")
    public ResponseEntity<ApiResponse<Void>> deleteWebhook(
            @PathVariable UUID workspaceId,
            @PathVariable UUID webhookId,
            @AuthenticationPrincipal UserPrincipal principal) {
        webhookService.deleteWebhook(workspaceId, webhookId, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Webhook deleted successfully", null));
    }

    @PostMapping("/{webhookId}/test")
    @Operation(summary = "Trigger a test webhook delivery")
    public ResponseEntity<ApiResponse<Boolean>> testWebhook(
            @PathVariable UUID workspaceId,
            @PathVariable UUID webhookId,
            @AuthenticationPrincipal UserPrincipal principal) {
        boolean ok = webhookService.testWebhook(workspaceId, webhookId, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Test webhook sent successfully", ok));
    }

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }
}
