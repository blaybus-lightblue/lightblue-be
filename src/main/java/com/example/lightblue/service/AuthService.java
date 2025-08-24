package com.example.lightblue.service;

import com.example.lightblue.dto.AuthResponse;
import com.example.lightblue.dto.LoginRequest;
import com.example.lightblue.dto.RegisterRequest;
import com.example.lightblue.model.Account;
import com.example.lightblue.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;
    private final NaverOauth2Service naverOauth2Service;

    public AuthResponse register(RegisterRequest request) {
        var user = new Account(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                "NORMAL"
        );
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }

    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.addToBlacklist(token);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Account) {
            Account account = (Account) authentication.getPrincipal();
            String naverAccessToken = account.getNaverAccessToken();

            if (naverAccessToken != null && !naverAccessToken.isEmpty()) {
                naverOauth2Service.revokeToken(naverAccessToken);
                account.setNaverAccessToken(null);
                repository.save(account);
            }
        }
    }

    public Account registerOrUpdateOAuth2User(String email, String nickname, String profileImage) {
        return repository.findByUsername(email)
                .map(account -> {
                    // Update existing user if needed
                    // account.setNickname(nickname);
                    // account.setProfileImage(profileImage);
                    return repository.save(account);
                })
                .orElseGet(() -> {
                    // Register new user
                    var newUser = new Account(
                            email, // Using email as username for OAuth2 users
                            passwordEncoder.encode("oauth2_user"), // Dummy password for OAuth2 users
                            "NORMAL" // Default type for social users
                    );
                    // newUser.setNickname(nickname);
                    // newUser.setProfileImage(profileImage);
                    return repository.save(newUser);
                });
    }
}
