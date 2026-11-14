package com.formgenius.analytics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FormAnalyticsRepository extends JpaRepository<FormAnalytics, UUID> {
    Optional<FormAnalytics> findByFormIdAndDate(UUID formId, LocalDate date);
    List<FormAnalytics> findByFormIdAndDateBetweenOrderByDateAsc(UUID formId, LocalDate startDate, LocalDate endDate);
    List<FormAnalytics> findByFormIdOrderByDateAsc(UUID formId);

    @Query("SELECT COALESCE(SUM(fa.views), 0) FROM FormAnalytics fa WHERE fa.form.id = :formId")
    long sumViewsByFormId(@Param("formId") UUID formId);

    @Query("SELECT COALESCE(SUM(fa.submissions), 0) FROM FormAnalytics fa WHERE fa.form.id = :formId")
    long sumSubmissionsByFormId(@Param("formId") UUID formId);
}
