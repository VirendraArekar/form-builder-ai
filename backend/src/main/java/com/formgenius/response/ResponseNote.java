package com.formgenius.response;

import com.formgenius.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "response_notes")
public class ResponseNote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private FormResponse response;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "note_text", nullable = false, columnDefinition = "TEXT")
    private String noteText;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public ResponseNote() {}

    public ResponseNote(UUID id, FormResponse response, User author, String noteText, Instant createdAt) {
        this.id = id;
        this.response = response;
        this.author = author;
        this.noteText = noteText;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public FormResponse getResponse() {
        return response;
    }

    public User getAuthor() {
        return author;
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

    public void setResponse(FormResponse response) {
        this.response = response;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public static ResponseNoteBuilder builder() {
        return new ResponseNoteBuilder();
    }
    public static class ResponseNoteBuilder {
        private UUID id;
        private FormResponse response;
        private User author;
        private String noteText;
        private Instant createdAt;

        public ResponseNoteBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public ResponseNoteBuilder response(FormResponse response) {
            this.response = response;
            return this;
        }
        public ResponseNoteBuilder author(User author) {
            this.author = author;
            return this;
        }
        public ResponseNoteBuilder noteText(String noteText) {
            this.noteText = noteText;
            return this;
        }
        public ResponseNoteBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ResponseNote build() {
            ResponseNote instance = new ResponseNote();
            instance.id = this.id;
            instance.response = this.response;
            instance.author = this.author;
            instance.noteText = this.noteText;
            instance.createdAt = this.createdAt;
            return instance;
        }
    }
}
