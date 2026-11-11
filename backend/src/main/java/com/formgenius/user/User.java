package com.formgenius.user;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "system_role", nullable = false)
    private String systemRole = "EDITOR";

    @Column(name = "is_email_verified", nullable = false)
    private boolean isEmailVerified = false;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "reset_token_expiry")
    private Instant resetTokenExpiry;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public User() {}

    public User(UUID id, String email, String passwordHash, String firstName, String lastName, String systemRole, boolean isEmailVerified, String verificationToken, String resetPasswordToken, Instant resetTokenExpiry, boolean isActive, boolean isDeleted, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.systemRole = systemRole;
        this.isEmailVerified = isEmailVerified;
        this.verificationToken = verificationToken;
        this.resetPasswordToken = resetPasswordToken;
        this.resetTokenExpiry = resetTokenExpiry;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSystemRole() {
        return systemRole;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public Instant getResetTokenExpiry() {
        return resetTokenExpiry;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isDeleted() {
        return isDeleted;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
    }

    public void setEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public void setResetTokenExpiry(Instant resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }
    public static class UserBuilder {
        private UUID id;
        private String email;
        private String passwordHash;
        private String firstName;
        private String lastName;
        private String systemRole = "EDITOR";
        private boolean isEmailVerified = false;
        private String verificationToken;
        private String resetPasswordToken;
        private Instant resetTokenExpiry;
        private boolean isActive = true;
        private boolean isDeleted = false;
        private Instant createdAt;
        private Instant updatedAt;

        public UserBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }
        public UserBuilder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }
        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public UserBuilder systemRole(String systemRole) {
            this.systemRole = systemRole;
            return this;
        }
        public UserBuilder isEmailVerified(boolean isEmailVerified) {
            this.isEmailVerified = isEmailVerified;
            return this;
        }
        public UserBuilder verificationToken(String verificationToken) {
            this.verificationToken = verificationToken;
            return this;
        }
        public UserBuilder resetPasswordToken(String resetPasswordToken) {
            this.resetPasswordToken = resetPasswordToken;
            return this;
        }
        public UserBuilder resetTokenExpiry(Instant resetTokenExpiry) {
            this.resetTokenExpiry = resetTokenExpiry;
            return this;
        }
        public UserBuilder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }
        public UserBuilder isDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }
        public UserBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public UserBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public User build() {
            User instance = new User();
            instance.id = this.id;
            instance.email = this.email;
            instance.passwordHash = this.passwordHash;
            instance.firstName = this.firstName;
            instance.lastName = this.lastName;
            instance.systemRole = this.systemRole;
            instance.isEmailVerified = this.isEmailVerified;
            instance.verificationToken = this.verificationToken;
            instance.resetPasswordToken = this.resetPasswordToken;
            instance.resetTokenExpiry = this.resetTokenExpiry;
            instance.isActive = this.isActive;
            instance.isDeleted = this.isDeleted;
            instance.createdAt = this.createdAt;
            instance.updatedAt = this.updatedAt;
            return instance;
        }
    }
}
