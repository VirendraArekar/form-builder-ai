package com.formgenius.notification;

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
@RequestMapping({"/api/notifications", "/api/v1/notifications"})
@Tag(name = "Notifications", description = "Endpoints for user alerts and response notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "List notifications for current user")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> listNotifications(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<NotificationDto> list = notificationService.listNotifications(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark single notification as read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        notificationService.markAsRead(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Marked as read", null));
    }

    @PatchMapping("/read-all")
    @Operation(summary = "Mark all notifications as read")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @AuthenticationPrincipal UserPrincipal principal) {
        notificationService.markAllAsRead(principal.getId());
        return ResponseEntity.ok(ApiResponse.success("All marked as read", null));
    }

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
}
