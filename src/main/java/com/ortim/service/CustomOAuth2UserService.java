package com.ortim.service;

import java.util.Objects;
import java.util.Optional;

import com.ortim.model.AuthProvider;
import com.ortim.model.Role;
import com.ortim.model.User;
import com.ortim.model.UserSocialAccount;
import com.ortim.repository.UserRepository;
import com.ortim.repository.UserSocialAccountRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final UserSocialAccountRepository socialAccountRepository;

    public CustomOAuth2UserService(UserRepository userRepository,
                                   UserSocialAccountRepository socialAccountRepository) {
        this.userRepository = userRepository;
        this.socialAccountRepository = socialAccountRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try{
            OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
            OAuth2User oauth2User = delegate.loadUser(userRequest);

            // Sağlayıcı bilgilerini alıyoruz (örneğin "google", "github").
            String providerName = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
            AuthProvider provider = AuthProvider.valueOf(providerName);

            // Sağlayıcıdan dönen attribute'ları çekiyoruz.
            // (Attribute isimleri sağlayıcıya göre farklılık gösterebilir; burada örnek kullanım var.)
            String email = oauth2User.getAttribute("email");
            if (email == null) {
                // Eğer email bilgisi yoksa hata fırlatıyoruz.
                throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
            }
            String fullName = oauth2User.getAttribute("name");
            // Google için "sub", GitHub için "id" olabilir.
            String providerId = oauth2User.getAttribute("sub") != null
                    ? oauth2User.getAttribute("sub")
                    : String.valueOf(Objects.requireNonNull(oauth2User.getAttribute("id")));
            String profileImageUrl = oauth2User.getAttribute("picture");

            // Kullanıcıyı email üzerinden buluyoruz, yoksa oluşturuyoruz.
            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        Role role = Role.builder()
                                .name("USER")
                                .description("Temel rol")
                                .build();
                        User user1 = new User();
                        user1.setEmail(email);
                        user1.setUsername(email);
                        user1.setName(fullName);
                        user1.getRoles().add(role);
                        user1.setIsActive(false);
                        user1.setIsDeleted(false);
                        return user1;
                    });

            user = userRepository.save(user);

            // Sosyal hesap bilgisini kontrol edip güncelliyoruz veya oluşturuyoruz.
            Optional<UserSocialAccount> socialAccountOpt = socialAccountRepository.findByProviderAndProviderId(provider, providerId);
            UserSocialAccount socialAccount = socialAccountOpt.orElseGet(UserSocialAccount::new);
            socialAccount.setProvider(provider);
            socialAccount.setProviderId(providerId);
            socialAccount.setFullName(fullName);
            socialAccount.setProfileImageUrl(profileImageUrl);
            socialAccount.setUser(user);

            socialAccountRepository.save(socialAccount);

            // Kullanıcı yetkilerini belirleyip OAuth2User olarak döndürüyoruz.
            return new DefaultOAuth2User(
                    user.getAuthorities(),
                    oauth2User.getAttributes(),
                    "email"  // kimlik belirleyici attribute (username yerine email kullanılabilir)
            );
        }catch (Exception e){
            throw new OAuth2AuthenticationException("Error loading user information");
        }

    }
}

