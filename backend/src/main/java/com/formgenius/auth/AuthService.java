package com.formgenius.auth;

import com.formgenius.auth.dto.*;
import com.formgenius.exception.BadRequestException;
import com.formgenius.exception.ResourceNotFoundException;
import com.formgenius.security.JwtTokenProvider;
import com.formgenius.user.User;
import com.formgenius.user.UserProfile;
import com.formgenius.user.UserProfileRepository;
import com.formgenius.user.UserRepository;
import com.formgenius.workspace.Workspace;
import com.formgenius.workspace.WorkspaceMember;
import com.formgenius.workspace.WorkspaceMemberRepository;
import com.formgenius.workspace.WorkspaceRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered: " + request.getEmail());
        }

        // Create User
        String fName = request.getFirstName() != null && !request.getFirstName().isBlank() ? request.getFirstName().trim() : "User";
        String lName = request.getLastName() != null ? request.getLastName().trim() : "";

        User user = User.builder()
                .email(request.getEmail().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(fName)
                .lastName(lName)
                .systemRole("EDITOR")
                .isEmailVerified(true) // Auto-verified for frictionless SaaS onboarding
                .isActive(true)
                .build();
        user = userRepository.save(user);

        // Create User Profile
        UserProfile profile = UserProfile.builder()
                .user(user)
                .companyName(request.getCompanyName())
                .preferredTheme("system")
                .build();
        userProfileRepository.save(profile);

        // Create Default Workspace
        String baseSlug = (user.getFirstName() + "-workspace").toLowerCase().replaceAll("[^a-z0-9]", "-");
        String slug = baseSlug + "-" + UUID.randomUUID().toString().substring(0, 6);
        Workspace workspace = Workspace.builder()
                .name(user.getFirstName() + "'s Workspace")
                .slug(slug)
                .owner(user)
                .planTier("FREE")
                .maxForms(5)
                .maxResponsesPerMonth(100)
                .aiGenerationsQuota(20)
                .build();
        workspace = workspaceRepository.save(workspace);

        // Add User as Workspace Owner
        WorkspaceMember member = WorkspaceMember.builder()
                .workspace(workspace)
                .user(user)
                .role("WORKSPACE_OWNER")
                .build();
        workspaceMemberRepository.save(member);

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getSystemRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return buildAuthResponse(user, workspace, "WORKSPACE_OWNER", accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase().trim(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.isActive() || user.isDeleted()) {
            throw new BadRequestException("This account is inactive or disabled");
        }

        List<Workspace> workspaces = workspaceRepository.findAllByUserId(user.getId());
        Workspace activeWorkspace = workspaces.isEmpty() ? null : workspaces.get(0);
        String role = "EDITOR";
        if (activeWorkspace != null) {
            role = workspaceMemberRepository.findByWorkspaceIdAndUserId(activeWorkspace.getId(), user.getId())
                    .map(WorkspaceMember::getRole)
                    .orElse("EDITOR");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getSystemRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return buildAuthResponse(user, activeWorkspace, role, accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            throw new BadRequestException("Invalid or expired refresh token");
        }

        UUID userId = jwtTokenProvider.getUserIdFromToken(request.getRefreshToken());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Workspace> workspaces = workspaceRepository.findAllByUserId(user.getId());
        Workspace activeWorkspace = workspaces.isEmpty() ? null : workspaces.get(0);
        String role = "EDITOR";
        if (activeWorkspace != null) {
            role = workspaceMemberRepository.findByWorkspaceIdAndUserId(activeWorkspace.getId(), user.getId())
                    .map(WorkspaceMember::getRole)
                    .orElse("EDITOR");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getSystemRole());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return buildAuthResponse(user, activeWorkspace, role, newAccessToken, newRefreshToken);
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail().toLowerCase().trim()).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            user.setResetPasswordToken(token);
            user.setResetTokenExpiry(Instant.now().plus(2, ChronoUnit.HOURS));
            userRepository.save(user);
            log.info("Password reset token generated for {}: {}", user.getEmail(), token);
        });
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired password reset token"));

        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(Instant.now())) {
            throw new BadRequestException("Password reset token has expired");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse getCurrentUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Workspace> workspaces = workspaceRepository.findAllByUserId(user.getId());
        Workspace activeWorkspace = workspaces.isEmpty() ? null : workspaces.get(0);
        String role = "EDITOR";
        if (activeWorkspace != null) {
            role = workspaceMemberRepository.findByWorkspaceIdAndUserId(activeWorkspace.getId(), user.getId())
                    .map(WorkspaceMember::getRole)
                    .orElse("EDITOR");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getSystemRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return buildAuthResponse(user, activeWorkspace, role, accessToken, refreshToken);
    }

    private AuthResponse buildAuthResponse(User user, Workspace workspace, String role, String accessToken, String refreshToken) {
        String avatarUrl = userProfileRepository.findByUserId(user.getId())
                .map(UserProfile::getAvatarUrl)
                .orElse(null);

        UserSummaryDto userSummary = UserSummaryDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .systemRole(user.getSystemRole())
                .avatarUrl(avatarUrl)
                .build();

        WorkspaceSummaryDto workspaceSummary = workspace != null ? WorkspaceSummaryDto.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .slug(workspace.getSlug())
                .role(role)
                .planTier(workspace.getPlanTier())
                .build() : null;

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(userSummary)
                .activeWorkspace(workspaceSummary)
                .build();
    }

    @Transactional
    public AuthResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            String[] parts = request.getName().trim().split("\\s+", 2);
            user.setFirstName(parts[0]);
            user.setLastName(parts.length > 1 ? parts[1] : "");
        } else {
            if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
                user.setFirstName(request.getFirstName().trim());
            }
            if (request.getLastName() != null) {
                user.setLastName(request.getLastName().trim());
            }
        }

        if (request.getEmail() != null && !request.getEmail().isBlank() && !request.getEmail().equalsIgnoreCase(user.getEmail())) {
            String newEmail = request.getEmail().trim().toLowerCase();
            if (userRepository.existsByEmail(newEmail)) {
                throw new BadRequestException("Email is already registered: " + newEmail);
            }
            user.setEmail(newEmail);
        }

        user = userRepository.save(user);

        UserProfile profile = userProfileRepository.findByUserId(userId).orElse(null);
        if (profile == null) {
            profile = UserProfile.builder().user(user).build();
        }
        if (request.getAvatar() != null) {
            profile.setAvatarUrl(request.getAvatar());
            userProfileRepository.save(profile);
        }

        return getCurrentUser(userId);
    }

    @Transactional
    public void changePassword(UUID userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Current password does not match.");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setDeleted(true);
        user.setActive(false);
        userRepository.save(user);
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository, UserProfileRepository userProfileRepository, WorkspaceRepository workspaceRepository, WorkspaceMemberRepository workspaceMemberRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }
}
