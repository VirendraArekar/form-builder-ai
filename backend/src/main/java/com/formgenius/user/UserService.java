package com.formgenius.user;

import com.formgenius.exception.BadRequestException;
import com.formgenius.exception.ResourceNotFoundException;
import com.formgenius.user.dto.ChangePasswordRequest;
import com.formgenius.user.dto.UpdateProfileRequest;
import com.formgenius.user.dto.UserProfileResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> userProfileRepository.save(UserProfile.builder().user(user).build()));

        return buildProfileResponse(user, profile);
    }

    @Transactional
    public UserProfileResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        userRepository.save(user);

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> UserProfile.builder().user(user).build());

        if (request.getAvatarUrl() != null) profile.setAvatarUrl(request.getAvatarUrl());
        if (request.getJobTitle() != null) profile.setJobTitle(request.getJobTitle());
        if (request.getCompanyName() != null) profile.setCompanyName(request.getCompanyName());
        if (request.getPhoneNumber() != null) profile.setPhoneNumber(request.getPhoneNumber());
        if (request.getTimezone() != null) profile.setTimezone(request.getTimezone());
        if (request.getPreferredTheme() != null) profile.setPreferredTheme(request.getPreferredTheme());

        profile = userProfileRepository.save(profile);
        return buildProfileResponse(user, profile);
    }

    @Transactional
    public void changePassword(UUID userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Current password does not match");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setActive(false);
        user.setDeleted(true);
        userRepository.save(user);
    }

    private UserProfileResponse buildProfileResponse(User user, UserProfile profile) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .systemRole(user.getSystemRole())
                .avatarUrl(profile.getAvatarUrl())
                .jobTitle(profile.getJobTitle())
                .companyName(profile.getCompanyName())
                .phoneNumber(profile.getPhoneNumber())
                .timezone(profile.getTimezone())
                .preferredTheme(profile.getPreferredTheme())
                .build();
    }

    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }
}
