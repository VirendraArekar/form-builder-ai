package com.formgenius.response;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FormResponseRepository extends JpaRepository<FormResponse, UUID> {
    List<FormResponse> findByFormIdOrderBySubmittedAtDesc(UUID formId);
    long countByFormId(UUID formId);

    @Query("SELECT r FROM FormResponse r WHERE r.form.id = :formId " +
            "AND (:status IS NULL OR r.status = :status) " +
            "AND (:isReviewed IS NULL OR r.isReviewed = :isReviewed)")
    Page<FormResponse> findByFormIdWithFilters(@Param("formId") UUID formId,
                                              @Param("status") String status,
                                              @Param("isReviewed") Boolean isReviewed,
                                              Pageable pageable);

    @Query("SELECT COUNT(r) FROM FormResponse r WHERE r.form.workspace.id = :workspaceId")
    long countByWorkspaceId(@Param("workspaceId") UUID workspaceId);
}
