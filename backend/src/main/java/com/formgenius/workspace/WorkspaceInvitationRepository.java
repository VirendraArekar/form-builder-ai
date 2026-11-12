package com.formgenius.workspace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceInvitationRepository extends JpaRepository<WorkspaceInvitation, UUID> {
    Optional<WorkspaceInvitation> findByToken(String token);
    List<WorkspaceInvitation> findByWorkspaceIdAndStatus(UUID workspaceId, String status);
}
