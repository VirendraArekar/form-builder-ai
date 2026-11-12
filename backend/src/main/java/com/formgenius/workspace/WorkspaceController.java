package com.formgenius.workspace;

import com.formgenius.common.ApiResponse;
import com.formgenius.security.UserPrincipal;
import com.formgenius.workspace.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/api/workspaces", "/api/v1/workspaces"})
@Tag(name = "Workspaces", description = "Endpoints for managing workspaces, memberships, and team invitations")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @GetMapping
    @Operation(summary = "List all workspaces for current user")
    public ResponseEntity<ApiResponse<List<WorkspaceResponse>>> listWorkspaces(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<WorkspaceResponse> list = workspaceService.listUserWorkspaces(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @PostMapping
    @Operation(summary = "Create a new workspace")
    public ResponseEntity<ApiResponse<WorkspaceResponse>> createWorkspace(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateWorkspaceRequest request) {
        WorkspaceResponse response = workspaceService.createWorkspace(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Workspace created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get workspace details by ID")
    public ResponseEntity<ApiResponse<WorkspaceResponse>> getWorkspace(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        WorkspaceResponse response = workspaceService.getWorkspace(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update workspace settings")
    public ResponseEntity<ApiResponse<WorkspaceResponse>> updateWorkspace(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateWorkspaceRequest request) {
        WorkspaceResponse response = workspaceService.updateWorkspace(id, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Workspace updated successfully", response));
    }

    @GetMapping("/{id}/members")
    @Operation(summary = "List members of workspace")
    public ResponseEntity<ApiResponse<List<WorkspaceMemberResponse>>> listMembers(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        List<WorkspaceMemberResponse> members = workspaceService.listMembers(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success(members));
    }

    @PostMapping("/{id}/members")
    @Operation(summary = "Invite a new member to workspace")
    public ResponseEntity<ApiResponse<WorkspaceMemberResponse>> inviteMember(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody InviteMemberRequest request) {
        WorkspaceMemberResponse member = workspaceService.inviteMember(id, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Invitation processed", member));
    }

    @PutMapping("/{id}/members/{memberId}")
    @Operation(summary = "Update a member's role")
    public ResponseEntity<ApiResponse<Void>> updateMemberRole(
            @PathVariable UUID id,
            @PathVariable UUID memberId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateMemberRoleRequest request) {
        workspaceService.updateMemberRole(id, memberId, principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Member role updated", null));
    }

    @DeleteMapping("/{id}/members/{memberId}")
    @Operation(summary = "Remove a member from workspace")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable UUID id,
            @PathVariable UUID memberId,
            @AuthenticationPrincipal UserPrincipal principal) {
        workspaceService.removeMember(id, memberId, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Member removed", null));
    }

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }
}
