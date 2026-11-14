package com.formgenius.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TemplateRepository extends JpaRepository<FormTemplate, UUID> {
    List<FormTemplate> findByCategoryIgnoreCase(String category);

    @Query("SELECT t FROM FormTemplate t WHERE " +
            "(:category IS NULL OR LOWER(t.category) = :category) AND " +
            "(:searchPattern IS NULL OR LOWER(t.title) LIKE :searchPattern OR LOWER(t.description) LIKE :searchPattern) " +
            "ORDER BY t.usageCount DESC")
    List<FormTemplate> findFiltered(@Param("category") String category, @Param("searchPattern") String searchPattern);
}
