package com.formgenius.form.dto;

import java.util.UUID;

public class FormLogicRuleDto {
    private String id;
    private String sourceQuestionId;
    private String targetQuestionId;
    private String targetSectionId;
    private String conditionOperator;
    private String conditionValue;
    private String actionType;
    private String logicalCombinator = "AND";
    private int ruleGroup = 0;

    public FormLogicRuleDto() {}

    public FormLogicRuleDto(String id, String sourceQuestionId, String targetQuestionId, String targetSectionId, String conditionOperator, String conditionValue, String actionType, String logicalCombinator, int ruleGroup) {
        this.id = id;
        this.sourceQuestionId = sourceQuestionId;
        this.targetQuestionId = targetQuestionId;
        this.targetSectionId = targetSectionId;
        this.conditionOperator = conditionOperator;
        this.conditionValue = conditionValue;
        this.actionType = actionType;
        this.logicalCombinator = logicalCombinator;
        this.ruleGroup = ruleGroup;
    }

    public FormLogicRuleDto(UUID id, UUID sourceQuestionId, UUID targetQuestionId, UUID targetSectionId, String conditionOperator, String conditionValue, String actionType, String logicalCombinator, int ruleGroup) {
        this(id != null ? id.toString() : null,
             sourceQuestionId != null ? sourceQuestionId.toString() : null,
             targetQuestionId != null ? targetQuestionId.toString() : null,
             targetSectionId != null ? targetSectionId.toString() : null,
             conditionOperator, conditionValue, actionType, logicalCombinator, ruleGroup);
    }

    public String getId() {
        return id;
    }

    public String getSourceQuestionId() {
        return sourceQuestionId;
    }

    public String getTargetQuestionId() {
        return targetQuestionId;
    }

    public String getTargetSectionId() {
        return targetSectionId;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setId(UUID id) {
        this.id = id != null ? id.toString() : null;
    }

    public void setSourceQuestionId(String sourceQuestionId) {
        this.sourceQuestionId = sourceQuestionId;
    }

    public void setSourceQuestionId(UUID sourceQuestionId) {
        this.sourceQuestionId = sourceQuestionId != null ? sourceQuestionId.toString() : null;
    }

    public void setTargetQuestionId(String targetQuestionId) {
        this.targetQuestionId = targetQuestionId;
    }

    public void setTargetQuestionId(UUID targetQuestionId) {
        this.targetQuestionId = targetQuestionId != null ? targetQuestionId.toString() : null;
    }

    public void setTargetSectionId(String targetSectionId) {
        this.targetSectionId = targetSectionId;
    }

    public void setTargetSectionId(UUID targetSectionId) {
        this.targetSectionId = targetSectionId != null ? targetSectionId.toString() : null;
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

    public static FormLogicRuleDtoBuilder builder() {
        return new FormLogicRuleDtoBuilder();
    }

    public static class FormLogicRuleDtoBuilder {
        private String id;
        private String sourceQuestionId;
        private String targetQuestionId;
        private String targetSectionId;
        private String conditionOperator;
        private String conditionValue;
        private String actionType;
        private String logicalCombinator = "AND";
        private int ruleGroup = 0;

        public FormLogicRuleDtoBuilder id(String id) {
            this.id = id;
            return this;
        }

        public FormLogicRuleDtoBuilder id(UUID id) {
            this.id = id != null ? id.toString() : null;
            return this;
        }

        public FormLogicRuleDtoBuilder sourceQuestionId(String sourceQuestionId) {
            this.sourceQuestionId = sourceQuestionId;
            return this;
        }

        public FormLogicRuleDtoBuilder sourceQuestionId(UUID sourceQuestionId) {
            this.sourceQuestionId = sourceQuestionId != null ? sourceQuestionId.toString() : null;
            return this;
        }

        public FormLogicRuleDtoBuilder targetQuestionId(String targetQuestionId) {
            this.targetQuestionId = targetQuestionId;
            return this;
        }

        public FormLogicRuleDtoBuilder targetQuestionId(UUID targetQuestionId) {
            this.targetQuestionId = targetQuestionId != null ? targetQuestionId.toString() : null;
            return this;
        }

        public FormLogicRuleDtoBuilder targetSectionId(String targetSectionId) {
            this.targetSectionId = targetSectionId;
            return this;
        }

        public FormLogicRuleDtoBuilder targetSectionId(UUID targetSectionId) {
            this.targetSectionId = targetSectionId != null ? targetSectionId.toString() : null;
            return this;
        }

        public FormLogicRuleDtoBuilder conditionOperator(String conditionOperator) {
            this.conditionOperator = conditionOperator;
            return this;
        }

        public FormLogicRuleDtoBuilder conditionValue(String conditionValue) {
            this.conditionValue = conditionValue;
            return this;
        }

        public FormLogicRuleDtoBuilder actionType(String actionType) {
            this.actionType = actionType;
            return this;
        }

        public FormLogicRuleDtoBuilder logicalCombinator(String logicalCombinator) {
            this.logicalCombinator = logicalCombinator;
            return this;
        }

        public FormLogicRuleDtoBuilder ruleGroup(int ruleGroup) {
            this.ruleGroup = ruleGroup;
            return this;
        }

        public FormLogicRuleDto build() {
            FormLogicRuleDto instance = new FormLogicRuleDto();
            instance.id = this.id;
            instance.sourceQuestionId = this.sourceQuestionId;
            instance.targetQuestionId = this.targetQuestionId;
            instance.targetSectionId = this.targetSectionId;
            instance.conditionOperator = this.conditionOperator;
            instance.conditionValue = this.conditionValue;
            instance.actionType = this.actionType;
            instance.logicalCombinator = this.logicalCombinator;
            instance.ruleGroup = this.ruleGroup;
            return instance;
        }
    }
}
