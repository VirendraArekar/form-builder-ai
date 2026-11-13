package com.formgenius.ai;

import com.formgenius.user.User;
import com.formgenius.workspace.Workspace;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ai_generation_history")
public class AiGenerationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String prompt;

    @Column(name = "generation_type", nullable = false)
    private String generationType; // FORM_GENERATION, QUESTION_IMPROVE, TRANSLATION, INSIGHTS

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "result_schema")
    private String resultSchema;

    @Column(name = "tokens_used")
    private int tokensUsed = 0;

    @Column(name = "latency_ms")
    private int latencyMs = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public AiGenerationHistory() {}

    public AiGenerationHistory(UUID id, Workspace workspace, User user, String prompt, String generationType, String modelName, String resultSchema, int tokensUsed, int latencyMs, Instant createdAt) {
        this.id = id;
        this.workspace = workspace;
        this.user = user;
        this.prompt = prompt;
        this.generationType = generationType;
        this.modelName = modelName;
        this.resultSchema = resultSchema;
        this.tokensUsed = tokensUsed;
        this.latencyMs = latencyMs;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public User getUser() {
        return user;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getGenerationType() {
        return generationType;
    }

    public String getModelName() {
        return modelName;
    }

    public String getResultSchema() {
        return resultSchema;
    }

    public int getTokensUsed() {
        return tokensUsed;
    }

    public int getLatencyMs() {
        return latencyMs;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setGenerationType(String generationType) {
        this.generationType = generationType;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setResultSchema(String resultSchema) {
        this.resultSchema = resultSchema;
    }

    public void setTokensUsed(int tokensUsed) {
        this.tokensUsed = tokensUsed;
    }

    public void setLatencyMs(int latencyMs) {
        this.latencyMs = latencyMs;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public static AiGenerationHistoryBuilder builder() {
        return new AiGenerationHistoryBuilder();
    }
    public static class AiGenerationHistoryBuilder {
        private UUID id;
        private Workspace workspace;
        private User user;
        private String prompt;
        private String generationType;
        private String modelName;
        private String resultSchema;
        private int tokensUsed = 0;
        private int latencyMs = 0;
        private Instant createdAt;

        public AiGenerationHistoryBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public AiGenerationHistoryBuilder workspace(Workspace workspace) {
            this.workspace = workspace;
            return this;
        }
        public AiGenerationHistoryBuilder user(User user) {
            this.user = user;
            return this;
        }
        public AiGenerationHistoryBuilder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }
        public AiGenerationHistoryBuilder generationType(String generationType) {
            this.generationType = generationType;
            return this;
        }
        public AiGenerationHistoryBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }
        public AiGenerationHistoryBuilder resultSchema(String resultSchema) {
            this.resultSchema = resultSchema;
            return this;
        }
        public AiGenerationHistoryBuilder tokensUsed(int tokensUsed) {
            this.tokensUsed = tokensUsed;
            return this;
        }
        public AiGenerationHistoryBuilder latencyMs(int latencyMs) {
            this.latencyMs = latencyMs;
            return this;
        }
        public AiGenerationHistoryBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AiGenerationHistory build() {
            AiGenerationHistory instance = new AiGenerationHistory();
            instance.id = this.id;
            instance.workspace = this.workspace;
            instance.user = this.user;
            instance.prompt = this.prompt;
            instance.generationType = this.generationType;
            instance.modelName = this.modelName;
            instance.resultSchema = this.resultSchema;
            instance.tokensUsed = this.tokensUsed;
            instance.latencyMs = this.latencyMs;
            instance.createdAt = this.createdAt;
            return instance;
        }
    }
}
