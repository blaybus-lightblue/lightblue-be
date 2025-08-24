package com.example.lightblue.service.oauth2;

import com.example.lightblue.global.code.status.ErrorStatus;
import com.example.lightblue.global.exception.GeneralException;
import com.example.lightblue.model.Account;
import com.example.lightblue.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AccountRepository accountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        log.info("registrationId: {}", registrationId);
        log.info("userNameAttributeName: {}", userNameAttributeName);
        log.info("oAuth2User attributes: {}", oAuth2User.getAttributes());

        String email = null;
        String nickname = null;
        String profileImage = null;

        if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
            email = (String) response.get("email");
            nickname = (String) response.get("nickname");
            profileImage = (String) response.get("profile_image");
        } else if ("google".equals(registrationId)) {
            email = oAuth2User.getAttribute("email");
            nickname = oAuth2User.getAttribute("name");
            profileImage = oAuth2User.getAttribute("picture");
        }

        if (email == null) {
            throw new GeneralException(ErrorStatus.EMAIL_NOT_FOUND);
        }

        final String finalEmail = email;
        final String finalNickname = nickname;
        final String finalProfileImage = profileImage;

        Account account = accountRepository.findByUsername(email)
                .orElseGet(() -> {
                    if (accountRepository.findByUsername(finalEmail).isPresent()) {
                        throw new GeneralException(ErrorStatus.EMAIL_DUPLICATE);
                    }
                    Account newAccount = new Account();
                    newAccount.setUsername(finalEmail);
                    newAccount.setPassword(UUID.randomUUID().toString());
                    newAccount.setAccountType("NORMAL");
                    newAccount.setNickname(finalNickname);
                    newAccount.setProfileImage(finalProfileImage);
                    return accountRepository.save(newAccount);
                });

        return new DefaultOAuth2User(
                account.getAuthorities(),
                oAuth2User.getAttributes(),
                userNameAttributeName
        );
    }
}
