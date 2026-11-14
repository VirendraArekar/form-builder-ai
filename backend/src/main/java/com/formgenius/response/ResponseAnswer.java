package com.formgenius.response;

import com.formgenius.form.FormQuestion;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "response_answers")
public class ResponseAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private FormResponse response;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private FormQuestion question;

    @Column(name = "question_key", nullable = false)
    private String questionKey;

    @Column(name = "answer_text", columnDefinition = "TEXT")
    private String answerText;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "answer_json")
    private String answerJson;

    public ResponseAnswer() {}

    public ResponseAnswer(UUID id, FormResponse response, FormQuestion question, String questionKey, String answerText, String answerJson) {
        this.id = id;
        this.response = response;
        this.question = question;
        this.questionKey = questionKey;
        this.answerText = answerText;
        this.answerJson = answerJson;
    }

    public UUID getId() {
        return id;
    }

    public FormResponse getResponse() {
        return response;
    }

    public FormQuestion getQuestion() {
        return question;
    }

    public String getQuestionKey() {
        return questionKey;
    }

    public String getAnswerText() {
        return answerText;
    }

    public String getAnswerJson() {
        return answerJson;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setResponse(FormResponse response) {
        this.response = response;
    }

    public void setQuestion(FormQuestion question) {
        this.question = question;
    }

    public void setQuestionKey(String questionKey) {
        this.questionKey = questionKey;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public void setAnswerJson(String answerJson) {
        this.answerJson = answerJson;
    }

    public static ResponseAnswerBuilder builder() {
        return new ResponseAnswerBuilder();
    }
    public static class ResponseAnswerBuilder {
        private UUID id;
        private FormResponse response;
        private FormQuestion question;
        private String questionKey;
        private String answerText;
        private String answerJson;

        public ResponseAnswerBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public ResponseAnswerBuilder response(FormResponse response) {
            this.response = response;
            return this;
        }
        public ResponseAnswerBuilder question(FormQuestion question) {
            this.question = question;
            return this;
        }
        public ResponseAnswerBuilder questionKey(String questionKey) {
            this.questionKey = questionKey;
            return this;
        }
        public ResponseAnswerBuilder answerText(String answerText) {
            this.answerText = answerText;
            return this;
        }
        public ResponseAnswerBuilder answerJson(String answerJson) {
            this.answerJson = answerJson;
            return this;
        }

        public ResponseAnswer build() {
            ResponseAnswer instance = new ResponseAnswer();
            instance.id = this.id;
            instance.response = this.response;
            instance.question = this.question;
            instance.questionKey = this.questionKey;
            instance.answerText = this.answerText;
            instance.answerJson = this.answerJson;
            return instance;
        }
    }
}
