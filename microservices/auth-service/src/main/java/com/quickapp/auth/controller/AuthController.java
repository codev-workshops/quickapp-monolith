package com.quickapp.auth.controller;

import com.quickapp.auth.dto.*;
import com.quickapp.auth.model.Permission;
import com.quickapp.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserVM> getCurrentUser(Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(authService.getCurrentUser(userId));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserVM>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserVM> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(authService.getUserById(id));
    }

    @PostMapping("/users")
    public ResponseEntity<UserVM> createUser(@RequestBody UserEditVM request) {
        return ResponseEntity.ok(authService.createUser(request));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserVM> updateUser(@PathVariable String id, @RequestBody UserEditVM request) {
        return ResponseEntity.ok(authService.updateUser(id, request));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        authService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/unblock/{id}")
    public ResponseEntity<UserVM> unblockUser(@PathVariable String id) {
        return ResponseEntity.ok(authService.unblockUser(id));
    }

    @GetMapping("/users/me/preferences")
    public ResponseEntity<String> getPreferences(Authentication authentication) {
        String userId = authentication.getName();
        String prefs = authService.getUserPreferences(userId);
        return ResponseEntity.ok(prefs != null ? prefs : "{}");
    }

    @PutMapping("/users/me/preferences")
    public ResponseEntity<Void> updatePreferences(Authentication authentication, @RequestBody String configuration) {
        String userId = authentication.getName();
        authService.updateUserPreferences(userId, configuration);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/permissions")
    public ResponseEntity<List<PermissionVM>> getAllPermissions() {
        List<PermissionVM> permissions = Arrays.stream(Permission.ALL_PERMISSIONS)
                .map(p -> new PermissionVM(p.getName(), p.getValue(), p.getGroupName(), p.getDescription()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(permissions);
    }
}
