package com.formgenius.form;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FormSectionRepository extends JpaRepository<FormSection, UUID> {
    List<FormSection> findByFormIdOrderByOrderIndexAsc(UUID formId);
    void deleteByFormId(UUID formId);
}
