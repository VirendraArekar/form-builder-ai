package com.formgenius.response;

import com.formgenius.form.FormQuestion;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "response_files")
public class ResponseFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private FormResponse response;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private FormQuestion question;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "file_size_bytes", nullable = false)
    private long fileSizeBytes;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @CreationTimestamp
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private Instant uploadedAt;

    public ResponseFile() {}

    public ResponseFile(UUID id, FormResponse response, FormQuestion question, String fileName, String fileUrl, long fileSizeBytes, String mimeType, Instant uploadedAt) {
        this.id = id;
        this.response = response;
        this.question = question;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileSizeBytes = fileSizeBytes;
        this.mimeType = mimeType;
        this.uploadedAt = uploadedAt;
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

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setFileSizeBytes(long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public static ResponseFileBuilder builder() {
        return new ResponseFileBuilder();
    }
    public static class ResponseFileBuilder {
        private UUID id;
        private FormResponse response;
        private FormQuestion question;
        private String fileName;
        private String fileUrl;
        private long fileSizeBytes;
        private String mimeType;
        private Instant uploadedAt;

        public ResponseFileBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public ResponseFileBuilder response(FormResponse response) {
            this.response = response;
            return this;
        }
        public ResponseFileBuilder question(FormQuestion question) {
            this.question = question;
            return this;
        }
        public ResponseFileBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }
        public ResponseFileBuilder fileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
            return this;
        }
        public ResponseFileBuilder fileSizeBytes(long fileSizeBytes) {
            this.fileSizeBytes = fileSizeBytes;
            return this;
        }
        public ResponseFileBuilder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }
        public ResponseFileBuilder uploadedAt(Instant uploadedAt) {
            this.uploadedAt = uploadedAt;
            return this;
        }

        public ResponseFile build() {
            ResponseFile instance = new ResponseFile();
            instance.id = this.id;
            instance.response = this.response;
            instance.question = this.question;
            instance.fileName = this.fileName;
            instance.fileUrl = this.fileUrl;
            instance.fileSizeBytes = this.fileSizeBytes;
            instance.mimeType = this.mimeType;
            instance.uploadedAt = this.uploadedAt;
            return instance;
        }
    }
}
