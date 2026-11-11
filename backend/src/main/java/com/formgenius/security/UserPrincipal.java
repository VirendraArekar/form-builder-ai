package com.formgenius.security;

import com.formgenius.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class UserPrincipal implements UserDetails {
    private final UUID id;
    private final String email;
    private final String password;
    private final String role;
    private final boolean active;

    public static UserPrincipal create(User user) {
        return UserPrincipal.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPasswordHash())
                .role(user.getSystemRole())
                .active(user.isActive())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public UserPrincipal(UUID id, String email, String password, String role, boolean active) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public static UserPrincipalBuilder builder() {
        return new UserPrincipalBuilder();
    }
    public static class UserPrincipalBuilder {
        private UUID id;
        private String email;
        private String password;
        private String role;
        private boolean active;

        public UserPrincipalBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public UserPrincipalBuilder email(String email) {
            this.email = email;
            return this;
        }
        public UserPrincipalBuilder password(String password) {
            this.password = password;
            return this;
        }
        public UserPrincipalBuilder role(String role) {
            this.role = role;
            return this;
        }
        public UserPrincipalBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public UserPrincipal build() {
            return new UserPrincipal(id, email, password, role, active);
        }
    }
}
