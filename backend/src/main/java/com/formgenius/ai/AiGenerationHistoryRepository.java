package com.formgenius.ai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AiGenerationHistoryRepository extends JpaRepository<AiGenerationHistory, UUID> {
    List<AiGenerationHistory> findByWorkspaceIdOrderByCreatedAtDesc(UUID workspaceId);
}
