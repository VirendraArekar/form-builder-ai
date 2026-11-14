package com.formgenius.response.dto;


import java.time.Instant;
import java.util.UUID;

public class ResponseNoteDto {
    private UUID id;
    private UUID authorId;
    private String authorName;
    private String noteText;
    private Instant createdAt;

    public ResponseNoteDto() {}

    public ResponseNoteDto(UUID id, UUID authorId, String authorName, String noteText, Instant createdAt) {
        this.id = id;
        this.authorId = authorId;
        this.authorName = authorName;
        this.noteText = noteText;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getNoteText() {
        return noteText;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public static ResponseNoteDtoBuilder builder() {
        return new ResponseNoteDtoBuilder();
    }
    public static class ResponseNoteDtoBuilder {
        private UUID id;
        private UUID authorId;
        private String authorName;
        private String noteText;
        private Instant createdAt;

        public ResponseNoteDtoBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public ResponseNoteDtoBuilder authorId(UUID authorId) {
            this.authorId = authorId;
            return this;
        }
        public ResponseNoteDtoBuilder authorName(String authorName) {
            this.authorName = authorName;
            return this;
        }
        public ResponseNoteDtoBuilder noteText(String noteText) {
            this.noteText = noteText;
            return this;
        }
        public ResponseNoteDtoBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ResponseNoteDto build() {
            ResponseNoteDto instance = new ResponseNoteDto();
            instance.id = this.id;
            instance.authorId = this.authorId;
            instance.authorName = this.authorName;
            instance.noteText = this.noteText;
            instance.createdAt = this.createdAt;
            return instance;
        }
    }
}
