package com.formgenius.workspace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
    Optional<Workspace> findBySlug(String slug);
    boolean existsBySlug(String slug);
    List<Workspace> findByOwnerIdAndIsDeletedFalse(UUID ownerId);

    @Query("SELECT wm.workspace FROM WorkspaceMember wm WHERE wm.user.id = :userId AND wm.workspace.isDeleted = false")
    List<Workspace> findAllByUserId(@Param("userId") UUID userId);
}
