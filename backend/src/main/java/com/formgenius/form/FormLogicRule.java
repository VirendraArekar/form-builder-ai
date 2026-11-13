package com.formgenius.form;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "form_logic_rules")
public class FormLogicRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_question_id", nullable = false)
    private FormQuestion sourceQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_question_id")
    private FormQuestion targetQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_section_id")
    private FormSection targetSection;

    @Column(name = "condition_operator", nullable = false)
    private String conditionOperator; // EQUALS, NOT_EQUALS, CONTAINS, GREATER_THAN, LESS_THAN, IS_EMPTY, IS_NOT_EMPTY

    @Column(name = "condition_value", columnDefinition = "TEXT")
    private String conditionValue;

    @Column(name = "action_type", nullable = false)
    private String actionType; // SHOW, HIDE, REQUIRE, JUMP_TO_SECTION

    @Column(name = "logical_combinator", nullable = false)
    private String logicalCombinator = "AND"; // AND, OR

    @Column(name = "rule_group", nullable = false)
    private int ruleGroup = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public FormLogicRule() {}

    public FormLogicRule(UUID id, Form form, FormQuestion sourceQuestion, FormQuestion targetQuestion, FormSection targetSection, String conditionOperator, String conditionValue, String actionType, String logicalCombinator, int ruleGroup, Instant createdAt) {
        this.id = id;
        this.form = form;
        this.sourceQuestion = sourceQuestion;
        this.targetQuestion = targetQuestion;
        this.targetSection = targetSection;
        this.conditionOperator = conditionOperator;
        this.conditionValue = conditionValue;
        this.actionType = actionType;
        this.logicalCombinator = logicalCombinator;
        this.ruleGroup = ruleGroup;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public FormQuestion getSourceQuestion() {
        return sourceQuestion;
    }

    public FormQuestion getTargetQuestion() {
        return targetQuestion;
    }

    public FormSection getTargetSection() {
        return targetSection;
    }

    public String getConditionOperator() {
        return conditionOperator;
    }

    public String getConditionValue() {
        return conditionValue;
    }

    public String getActionType() {
        return actionType;
    }

    public String getLogicalCombinator() {
        return logicalCombinator;
    }

    public int getRuleGroup() {
        return ruleGroup;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public void setSourceQuestion(FormQuestion sourceQuestion) {
        this.sourceQuestion = sourceQuestion;
    }

    public void setTargetQuestion(FormQuestion targetQuestion) {
        this.targetQuestion = targetQuestion;
    }

    public void setTargetSection(FormSection targetSection) {
        this.targetSection = targetSection;
    }

    public void setConditionOperator(String conditionOperator) {
        this.conditionOperator = conditionOperator;
    }

    public void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setLogicalCombinator(String logicalCombinator) {
        this.logicalCombinator = logicalCombinator;
    }

    public void setRuleGroup(int ruleGroup) {
        this.ruleGroup = ruleGroup;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public static FormLogicRuleBuilder builder() {
        return new FormLogicRuleBuilder();
    }
    public static class FormLogicRuleBuilder {
        private UUID id;
        private Form form;
        private FormQuestion sourceQuestion;
        private FormQuestion targetQuestion;
        private FormSection targetSection;
        private String conditionOperator;
        private String conditionValue;
        private String actionType;
        private String logicalCombinator = "AND";
        private int ruleGroup = 0;
        private Instant createdAt;

        public FormLogicRuleBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public FormLogicRuleBuilder form(Form form) {
            this.form = form;
            return this;
        }
        public FormLogicRuleBuilder sourceQuestion(FormQuestion sourceQuestion) {
            this.sourceQuestion = sourceQuestion;
            return this;
        }
        public FormLogicRuleBuilder targetQuestion(FormQuestion targetQuestion) {
            this.targetQuestion = targetQuestion;
            return this;
        }
        public FormLogicRuleBuilder targetSection(FormSection targetSection) {
            this.targetSection = targetSection;
            return this;
        }
        public FormLogicRuleBuilder conditionOperator(String conditionOperator) {
            this.conditionOperator = conditionOperator;
            return this;
        }
        public FormLogicRuleBuilder conditionValue(String conditionValue) {
            this.conditionValue = conditionValue;
            return this;
        }
        public FormLogicRuleBuilder actionType(String actionType) {
            this.actionType = actionType;
            return this;
        }
        public FormLogicRuleBuilder logicalCombinator(String logicalCombinator) {
            this.logicalCombinator = logicalCombinator;
            return this;
        }
        public FormLogicRuleBuilder ruleGroup(int ruleGroup) {
            this.ruleGroup = ruleGroup;
            return this;
        }
        public FormLogicRuleBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public FormLogicRule build() {
            FormLogicRule instance = new FormLogicRule();
            instance.id = this.id;
            instance.form = this.form;
            instance.sourceQuestion = this.sourceQuestion;
            instance.targetQuestion = this.targetQuestion;
            instance.targetSection = this.targetSection;
            instance.conditionOperator = this.conditionOperator;
            instance.conditionValue = this.conditionValue;
            instance.actionType = this.actionType;
            instance.logicalCombinator = this.logicalCombinator;
            instance.ruleGroup = this.ruleGroup;
            instance.createdAt = this.createdAt;
            return instance;
        }
    }
}
