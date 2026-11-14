package com.formgenius.analytics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceAnalyticsRepository extends JpaRepository<WorkspaceAnalytics, UUID> {
    Optional<WorkspaceAnalytics> findByWorkspaceIdAndDate(UUID workspaceId, LocalDate date);
    List<WorkspaceAnalytics> findByWorkspaceIdAndDateBetweenOrderByDateAsc(UUID workspaceId, LocalDate startDate, LocalDate endDate);
}
