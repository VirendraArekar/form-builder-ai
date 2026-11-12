package com.formgenius.form;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FormQuestionOptionRepository extends JpaRepository<FormQuestionOption, UUID> {
    List<FormQuestionOption> findByQuestionIdOrderByOrderIndexAsc(UUID questionId);
    void deleteByQuestionId(UUID questionId);
}
