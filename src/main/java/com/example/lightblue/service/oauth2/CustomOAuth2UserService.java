package com.example.lightblue.service.oauth2;

import com.example.lightblue.model.Account;
import com.example.lightblue.repository.AccountRepository;
import com.example.lightblue.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AccountRepository accountRepository;
    private final AuthService authService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        log.info("registrationId: {}", registrationId);
        log.info("userNameAttributeName: {}", userNameAttributeName);
        log.info("oAuth2User attributes: {}", oAuth2User.getAttributes());

        // Naver specific user info extraction
        if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
            String email = (String) response.get("email");
            String nickname = (String) response.get("nickname");
            String profileImage = (String) response.get("profile_image");

            log.info("Naver User Info - Email: {}, Nickname: {}, Profile Image: {}", email, nickname, profileImage);

            Account account = authService.registerOrUpdateOAuth2User(email, nickname, profileImage);

            return new DefaultOAuth2User(
                    account.getAuthorities(),
                    oAuth2User.getAttributes(),
                    userNameAttributeName
            );
        }

        return new DefaultOAuth2User(Collections.emptyList(), oAuth2User.getAttributes(), userNameAttributeName);
    }
}
