package com.formgenius.form;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FormThemeRepository extends JpaRepository<FormTheme, UUID> {
    Optional<FormTheme> findByFormId(UUID formId);
    void deleteByFormId(UUID formId);
}
