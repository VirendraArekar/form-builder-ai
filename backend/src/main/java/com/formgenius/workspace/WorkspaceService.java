package com.formgenius.workspace;

import com.formgenius.exception.BadRequestException;
import com.formgenius.exception.ForbiddenException;
import com.formgenius.exception.ResourceNotFoundException;
import com.formgenius.form.FormRepository;
import com.formgenius.user.User;
import com.formgenius.user.UserProfile;
import com.formgenius.user.UserProfileRepository;
import com.formgenius.user.UserRepository;
import com.formgenius.workspace.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final WorkspaceInvitationRepository workspaceInvitationRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final FormRepository formRepository;

    @Transactional(readOnly = true)
    public List<WorkspaceResponse> listUserWorkspaces(UUID userId) {
        List<Workspace> workspaces = workspaceRepository.findAllByUserId(userId);
        return workspaces.stream()
                .map(ws -> mapToResponse(ws, userId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WorkspaceResponse getWorkspace(UUID workspaceId, UUID userId) {
        Workspace workspace = findAndValidateMember(workspaceId, userId);
        return mapToResponse(workspace, userId);
    }

    @Transactional
    public WorkspaceResponse createWorkspace(UUID userId, CreateWorkspaceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String slug = request.getName().toLowerCase().replaceAll("[^a-z0-9]", "-") + "-" + UUID.randomUUID().toString().substring(0, 6);

        Workspace workspace = Workspace.builder()
                .name(request.getName().trim())
                .slug(slug)
                .description(request.getDescription())
                .logoUrl(request.getLogoUrl())
                .owner(user)
                .planTier("FREE")
                .maxForms(5)
                .maxResponsesPerMonth(100)
                .aiGenerationsQuota(20)
                .build();
        workspace = workspaceRepository.save(workspace);

        WorkspaceMember member = WorkspaceMember.builder()
                .workspace(workspace)
                .user(user)
                .role("WORKSPACE_OWNER")
                .build();
        workspaceMemberRepository.save(member);

        return mapToResponse(workspace, userId);
    }

    @Transactional
    public WorkspaceResponse updateWorkspace(UUID workspaceId, UUID userId, UpdateWorkspaceRequest request) {
        Workspace workspace = findAndValidateAdmin(workspaceId, userId);

        workspace.setName(request.getName().trim());
        workspace.setDescription(request.getDescription());
        if (request.getLogoUrl() != null) {
            workspace.setLogoUrl(request.getLogoUrl());
        }

        workspace = workspaceRepository.save(workspace);
        return mapToResponse(workspace, userId);
    }

    @Transactional(readOnly = true)
    public List<WorkspaceMemberResponse> listMembers(UUID workspaceId, UUID userId) {
        findAndValidateMember(workspaceId, userId);
        List<WorkspaceMember> members = workspaceMemberRepository.findByWorkspaceId(workspaceId);

        return members.stream().map(m -> {
            User u = m.getUser();
            String avatarUrl = userProfileRepository.findByUserId(u.getId())
                    .map(UserProfile::getAvatarUrl)
                    .orElse(null);
            return WorkspaceMemberResponse.builder()
                    .id(m.getId())
                    .userId(u.getId())
                    .email(u.getEmail())
                    .firstName(u.getFirstName())
                    .lastName(u.getLastName())
                    .avatarUrl(avatarUrl)
                    .role(m.getRole())
                    .joinedAt(m.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional
    public WorkspaceMemberResponse inviteMember(UUID workspaceId, UUID userId, InviteMemberRequest request) {
        Workspace workspace = findAndValidateAdmin(workspaceId, userId);
        User inviter = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // If user already exists in the system, add directly or create invitation
        String email = request.getEmail().toLowerCase().trim();
        User existingUser = userRepository.findByEmail(email).orElse(null);

        if (existingUser != null) {
            if (workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, existingUser.getId())) {
                throw new BadRequestException("User is already a member of this workspace");
            }
            WorkspaceMember member = WorkspaceMember.builder()
                    .workspace(workspace)
                    .user(existingUser)
                    .role(request.getRole())
                    .build();
            member = workspaceMemberRepository.save(member);

            String avatarUrl = userProfileRepository.findByUserId(existingUser.getId())
                    .map(UserProfile::getAvatarUrl)
                    .orElse(null);

            return WorkspaceMemberResponse.builder()
                    .id(member.getId())
                    .userId(existingUser.getId())
                    .email(existingUser.getEmail())
                    .firstName(existingUser.getFirstName())
                    .lastName(existingUser.getLastName())
                    .avatarUrl(avatarUrl)
                    .role(member.getRole())
                    .joinedAt(member.getCreatedAt())
                    .build();
        } else {
            // Record invitation
            WorkspaceInvitation invitation = WorkspaceInvitation.builder()
                    .workspace(workspace)
                    .email(email)
                    .role(request.getRole())
                    .token(UUID.randomUUID().toString())
                    .invitedBy(inviter)
                    .status("PENDING")
                    .expiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
                    .build();
            workspaceInvitationRepository.save(invitation);

            return WorkspaceMemberResponse.builder()
                    .id(invitation.getId())
                    .email(email)
                    .firstName("Invited")
                    .lastName("Member")
                    .role(request.getRole())
                    .joinedAt(invitation.getCreatedAt())
                    .build();
        }
    }

    @Transactional
    public void updateMemberRole(UUID workspaceId, UUID memberId, UUID userId, UpdateMemberRoleRequest request) {
        findAndValidateAdmin(workspaceId, userId);
        WorkspaceMember member = workspaceMemberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member record not found"));

        if (!member.getWorkspace().getId().equals(workspaceId)) {
            throw new BadRequestException("Member does not belong to this workspace");
        }

        if ("WORKSPACE_OWNER".equals(member.getRole())) {
            throw new BadRequestException("Cannot alter role of Workspace Owner directly");
        }

        member.setRole(request.getRole());
        workspaceMemberRepository.save(member);
    }

    @Transactional
    public void removeMember(UUID workspaceId, UUID memberId, UUID userId) {
        findAndValidateAdmin(workspaceId, userId);
        WorkspaceMember member = workspaceMemberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member record not found"));

        if (!member.getWorkspace().getId().equals(workspaceId)) {
            throw new BadRequestException("Member does not belong to this workspace");
        }

        if ("WORKSPACE_OWNER".equals(member.getRole())) {
            throw new BadRequestException("Cannot remove the Workspace Owner");
        }

        workspaceMemberRepository.delete(member);
    }

    public Workspace findAndValidateMember(UUID workspaceId, UUID userId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
        if (!workspaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, userId)) {
            throw new ForbiddenException("You do not have access to this workspace");
        }
        return workspace;
    }

    public Workspace findAndValidateAdmin(UUID workspaceId, UUID userId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
        WorkspaceMember member = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, userId)
                .orElseThrow(() -> new ForbiddenException("You do not have access to this workspace"));
        if (!"WORKSPACE_OWNER".equals(member.getRole()) && !"ADMIN".equals(member.getRole())) {
            throw new ForbiddenException("Admin or Owner permission required for this action");
        }
        return workspace;
    }

    private WorkspaceResponse mapToResponse(Workspace ws, UUID userId) {
        String role = workspaceMemberRepository.findByWorkspaceIdAndUserId(ws.getId(), userId)
                .map(WorkspaceMember::getRole)
                .orElse("VIEWER");
        int memberCount = workspaceMemberRepository.findByWorkspaceId(ws.getId()).size();
        long formCount = formRepository.countByWorkspaceIdAndIsDeletedFalse(ws.getId());

        return WorkspaceResponse.builder()
                .id(ws.getId())
                .name(ws.getName())
                .slug(ws.getSlug())
                .description(ws.getDescription())
                .logoUrl(ws.getLogoUrl())
                .ownerId(ws.getOwner().getId())
                .ownerName(ws.getOwner().getFirstName() + " " + ws.getOwner().getLastName())
                .planTier(ws.getPlanTier())
                .maxForms(ws.getMaxForms())
                .maxResponsesPerMonth(ws.getMaxResponsesPerMonth())
                .aiGenerationsQuota(ws.getAiGenerationsQuota())
                .aiGenerationsUsed(ws.getAiGenerationsUsed())
                .memberCount(memberCount)
                .formCount((int) formCount)
                .userRole(role)
                .createdAt(ws.getCreatedAt())
                .build();
    }

    public WorkspaceService(WorkspaceRepository workspaceRepository, WorkspaceMemberRepository workspaceMemberRepository, WorkspaceInvitationRepository workspaceInvitationRepository, UserRepository userRepository, UserProfileRepository userProfileRepository, FormRepository formRepository) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.workspaceInvitationRepository = workspaceInvitationRepository;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.formRepository = formRepository;
    }
}
