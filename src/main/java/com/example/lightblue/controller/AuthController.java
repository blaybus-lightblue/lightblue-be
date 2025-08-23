package com.example.lightblue.controller;

import com.example.lightblue.dto.AuthResponse;
import com.example.lightblue.dto.LoginRequest;
import com.example.lightblue.dto.RegisterRequest;
import com.example.lightblue.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "기존 사용자로 로그인합니다.")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }
}