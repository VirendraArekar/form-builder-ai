package com.formgenius.form;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FormRepository extends JpaRepository<Form, UUID> {
    Optional<Form> findByPublishedSlugAndIsDeletedFalse(String slug);
    boolean existsByPublishedSlug(String slug);
    long countByWorkspaceIdAndIsDeletedFalse(UUID workspaceId);
    java.util.List<Form> findByWorkspaceIdAndIsDeletedFalse(UUID workspaceId);

    @Query("SELECT f FROM Form f WHERE f.workspace.id = :workspaceId AND f.isDeleted = false " +
            "AND (:status IS NULL OR f.status = :status) " +
            "AND (:isFavorite IS NULL OR f.isFavorite = :isFavorite) " +
            "AND (:searchPattern IS NULL OR LOWER(f.title) LIKE :searchPattern)")
    Page<Form> findAllByWorkspaceWithFilters(@Param("workspaceId") UUID workspaceId,
                                            @Param("status") String status,
                                            @Param("isFavorite") Boolean isFavorite,
                                            @Param("searchPattern") String searchPattern,
                                            Pageable pageable);
}
