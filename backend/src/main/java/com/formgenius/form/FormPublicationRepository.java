package com.formgenius.form;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FormPublicationRepository extends JpaRepository<FormPublication, UUID> {
    List<FormPublication> findByFormIdOrderByPublishedAtDesc(UUID formId);
    Optional<FormPublication> findByPublishedSlugAndIsActiveTrue(String slug);
}
