package com.formgenius.user;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "timezone")
    private String timezone = "UTC";

    @Column(name = "preferred_theme")
    private String preferredTheme = "system";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public UserProfile() {}

    public UserProfile(UUID id, User user, String avatarUrl, String jobTitle, String companyName, String phoneNumber, String timezone, String preferredTheme, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.user = user;
        this.avatarUrl = avatarUrl;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.timezone = timezone;
        this.preferredTheme = preferredTheme;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getPreferredTheme() {
        return preferredTheme;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setPreferredTheme(String preferredTheme) {
        this.preferredTheme = preferredTheme;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static UserProfileBuilder builder() {
        return new UserProfileBuilder();
    }
    public static class UserProfileBuilder {
        private UUID id;
        private User user;
        private String avatarUrl;
        private String jobTitle;
        private String companyName;
        private String phoneNumber;
        private String timezone = "UTC";
        private String preferredTheme = "system";
        private Instant createdAt;
        private Instant updatedAt;

        public UserProfileBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public UserProfileBuilder user(User user) {
            this.user = user;
            return this;
        }
        public UserProfileBuilder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }
        public UserProfileBuilder jobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
            return this;
        }
        public UserProfileBuilder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }
        public UserProfileBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }
        public UserProfileBuilder timezone(String timezone) {
            this.timezone = timezone;
            return this;
        }
        public UserProfileBuilder preferredTheme(String preferredTheme) {
            this.preferredTheme = preferredTheme;
            return this;
        }
        public UserProfileBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public UserProfileBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public UserProfile build() {
            UserProfile instance = new UserProfile();
            instance.id = this.id;
            instance.user = this.user;
            instance.avatarUrl = this.avatarUrl;
            instance.jobTitle = this.jobTitle;
            instance.companyName = this.companyName;
            instance.phoneNumber = this.phoneNumber;
            instance.timezone = this.timezone;
            instance.preferredTheme = this.preferredTheme;
            instance.createdAt = this.createdAt;
            instance.updatedAt = this.updatedAt;
            return instance;
        }
    }
}
