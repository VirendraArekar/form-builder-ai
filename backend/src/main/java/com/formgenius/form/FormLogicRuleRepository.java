package com.formgenius.form;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FormLogicRuleRepository extends JpaRepository<FormLogicRule, UUID> {
    List<FormLogicRule> findByFormId(UUID formId);
    void deleteByFormId(UUID formId);
}
