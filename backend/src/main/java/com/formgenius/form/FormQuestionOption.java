package com.formgenius.form;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "form_question_options")
public class FormQuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private FormQuestion question;

    @Column(nullable = false)
    private String label;

    @Column(name = "\"value\"", nullable = false)
    private String value;

    @Column(name = "order_index", nullable = false)
    private int orderIndex = 0;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault = false;

    public FormQuestionOption() {}

    public FormQuestionOption(UUID id, FormQuestion question, String label, String value, int orderIndex, boolean isDefault) {
        this.id = id;
        this.question = question;
        this.label = label;
        this.value = value;
        this.orderIndex = orderIndex;
        this.isDefault = isDefault;
    }

    public UUID getId() {
        return id;
    }

    public FormQuestion getQuestion() {
        return question;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setQuestion(FormQuestion question) {
        this.question = question;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public static FormQuestionOptionBuilder builder() {
        return new FormQuestionOptionBuilder();
    }
    public static class FormQuestionOptionBuilder {
        private UUID id;
        private FormQuestion question;
        private String label;
        private String value;
        private int orderIndex = 0;
        private boolean isDefault = false;

        public FormQuestionOptionBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public FormQuestionOptionBuilder question(FormQuestion question) {
            this.question = question;
            return this;
        }
        public FormQuestionOptionBuilder label(String label) {
            this.label = label;
            return this;
        }
        public FormQuestionOptionBuilder value(String value) {
            this.value = value;
            return this;
        }
        public FormQuestionOptionBuilder orderIndex(int orderIndex) {
            this.orderIndex = orderIndex;
            return this;
        }
        public FormQuestionOptionBuilder isDefault(boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }

        public FormQuestionOption build() {
            FormQuestionOption instance = new FormQuestionOption();
            instance.id = this.id;
            instance.question = this.question;
            instance.label = this.label;
            instance.value = this.value;
            instance.orderIndex = this.orderIndex;
            instance.isDefault = this.isDefault;
            return instance;
        }
    }
}
