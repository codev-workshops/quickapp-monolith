package com.quickapp.auth.service;

import com.quickapp.auth.dto.*;
import com.quickapp.auth.model.Role;
import com.quickapp.auth.model.User;
import com.quickapp.auth.repository.RoleRepository;
import com.quickapp.auth.repository.UserRepository;
import com.quickapp.auth.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        if (!user.getIsEnabled()) {
            throw new RuntimeException("User account is disabled");
        }

        return generateTokens(user);
    }

    public LoginResponse refreshToken(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (user.getRefreshTokenExpiry() == null || user.getRefreshTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return generateTokens(user);
    }

    private LoginResponse generateTokens(User user) {
        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        List<String> permissions = user.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .distinct()
                .collect(Collectors.toList());

        String accessToken = tokenProvider.generateAccessToken(user.getId(), user.getUserName(), roles, permissions);
        String newRefreshToken = tokenProvider.generateRefreshToken();

        user.setRefreshToken(newRefreshToken);
        user.setRefreshTokenExpiry(LocalDateTime.now().plusSeconds(tokenProvider.getRefreshExpirationMs() / 1000));
        userRepository.save(user);

        return new LoginResponse(accessToken, newRefreshToken, tokenProvider.getJwtExpirationMs() / 1000);
    }

    public UserVM getCurrentUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toUserVM(user);
    }

    public List<UserVM> getAllUsers() {
        return userRepository.findAll().stream().map(this::toUserVM).collect(Collectors.toList());
    }

    public UserVM getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toUserVM(user);
    }

    public UserVM createUser(UserEditVM request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUserName(request.getUserName());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setJobTitle(request.getJobTitle());
        user.setIsEnabled(request.getIsEnabled());
        user.setPasswordHash(passwordEncoder.encode(
                request.getNewPassword() != null ? request.getNewPassword() : "tempP@ss123"));

        if (request.getRoles() != null) {
            Set<Role> roles = request.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        user = userRepository.save(user);
        return toUserVM(user);
    }

    public UserVM updateUser(String id, UserEditVM request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getJobTitle() != null) user.setJobTitle(request.getJobTitle());
        user.setIsEnabled(request.getIsEnabled());

        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        }

        if (request.getRoles() != null) {
            Set<Role> roles = request.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        user = userRepository.save(user);
        return toUserVM(user);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public UserVM unblockUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsLockedOut(false);
        user = userRepository.save(user);
        return toUserVM(user);
    }

    public String getUserPreferences(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getConfiguration();
    }

    public void updateUserPreferences(String userId, String configuration) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setConfiguration(configuration);
        userRepository.save(user);
    }

    private UserVM toUserVM(User user) {
        UserVM vm = new UserVM();
        vm.setId(user.getId());
        vm.setUserName(user.getUserName());
        vm.setFullName(user.getFullName());
        vm.setEmail(user.getEmail());
        vm.setJobTitle(user.getJobTitle());
        vm.setPhoneNumber(user.getPhoneNumber());
        vm.setIsEnabled(user.getIsEnabled());
        vm.setIsLockedOut(user.getIsLockedOut());
        vm.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        return vm;
    }
}
