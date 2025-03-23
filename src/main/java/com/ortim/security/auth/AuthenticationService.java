package com.ortim.security.auth;

import com.ortim.component.MessageService;
import com.ortim.core.exception.ErrorCode;
import com.ortim.core.exception.GenericException;
import com.ortim.core.results.*;
import com.ortim.core.sendmail.EmailService;
import com.ortim.core.sendmail.MailObject;
import com.ortim.core.utils.EntityConstants;
import com.ortim.dto.response.UserResponse;
import com.ortim.model.Role;
import com.ortim.model.User;
import com.ortim.repository.RoleRepository;
import com.ortim.repository.UserRepository;
import com.ortim.security.jwt.JwtTokenBlackList;
import com.ortim.security.jwt.JwtTokenProvider;
import com.ortim.security.request.LoginRequest;
import com.ortim.security.request.RegisterRequest;
import com.ortim.security.response.AuthenticationResponse;
import com.ortim.security.response.RegisterResponse;
import com.ortim.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${base.url}")
    private String baseUrl;

    @Value("${client.base.url}")
    private String clientBaseUrl;

    @Value("${spring.mail.username}")
    private String rootMail;

    @Value("${settings.email.validate.isRequired}")
    private boolean isRequiredEmailValidation;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final MessageService messageService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final UserService userService;

    private final JwtTokenBlackList jwtTokenBlackList;

    private final RoleRepository roleRepository;

    public DataResult<RegisterResponse> register(RegisterRequest registerRequest, HttpServletResponse response) {
        if(SecurityManager.getCurrentUser() != null){
            return new ErrorDataResult<>(
                    null,
                    messageService.getMessage("user.register.already.login.message", null)
            );
        }
        if(userRepository.findByEmailAddress(registerRequest.getEmail()).isPresent()){
            //Hesabı silinen kullanıcı kontrolü yapılacak
            return new ErrorDataResult<>(
                    null,
                    messageService.getMessage("user.email.already_register", null)
            );
        }
        try{
           User user = User.builder()
                   .email(registerRequest.getEmail())
                   .password(passwordEncoder.encode(registerRequest.getPassword()))
                   .username(userService.generateUniqueUsername(registerRequest.getEmail()))
                   .isActive(false)
                   .isDeleted(false)
                   .build();

            userRepository.save(user);

            if(isRequiredEmailValidation){
                log.info(messageService.getMessage("user.register.success", new Object[]{user.getEmail()}));
                sendEmailConfirmation(user);

            }else{
                log.info(messageService.getMessage("user.register.success.without.email.validation.message", new Object[]{user.getEmail()}));
                user.setIsActive(true);
                Role role = roleRepository.findByName(EntityConstants.baseRoleName);
                assignRoleToUser(user.getId(), role.getId());
            }
            RegisterResponse registerResponse = RegisterResponse.builder()
                    .email(user.getEmail())
                    .isRequiredEmailValidation(isRequiredEmailValidation)
                    .build();
            return new SuccessDataResult<>(
                    registerResponse,
                    messageService.getMessage(isRequiredEmailValidation ? "user.register.success" : "user.register.success.without.email.validation.message", new Object[]{user.getEmail()})
            );
        }catch (Exception e){
            log.error(messageService.getMessage("user.register.error", null), e);
            return new ErrorDataResult<>(
                    null,
                    messageService.getMessage("user.register.error", null)
            );
        }
    }

    public DataResult<AuthenticationResponse> login(LoginRequest loginRequest, HttpServletResponse response){
       try{
           User user = userRepository.findByEmailAddress(loginRequest.getEmail()).orElse(null);
           if(user == null){
               log.error(messageService.getMessage("error.not_found", new Object[]{loginRequest.getEmail()}));
               return new ErrorDataResult<>(
                       null,
                       messageService.getMessage("error.not_found", new Object[]{loginRequest.getEmail()})
               );
           }
           if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
               log.error(messageService.getMessage("user.login.password.false.message", new Object[]{loginRequest.getEmail()}));
               return new ErrorDataResult<>(null, messageService.getMessage("user.login.password.false.message", new Object[]{loginRequest.getEmail()}));
           }
           AuthenticationResponse authResponse = authenticationResponse(user);
           if(user.getIsDeleted()){
               //Kullanıcı silinmiş durumda. bu mantığı işleteceğiz
               log.error(messageService.getMessage("user.login.isDeleted.true", new Object[]{loginRequest.getEmail()}));
               return new ErrorDataResult<>(
                       authResponse,
                       messageService.getMessage("user.login.isDeleted.true", null)
               );
           }
           if(!user.getIsActive()){
               log.error(messageService.getMessage("user.login.isActive.false", new Object[]{loginRequest.getEmail()}));
               return new ErrorDataResult<>(
                       authResponse,
                       messageService.getMessage("user.login.isActive.false", null)
               );
           }

           Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           loginRequest.getEmail(),
                           loginRequest.getPassword()
                   )
           );
           if (authentication == null || !authentication.isAuthenticated()) {
               log.error("Kullanıcı doğrulama başarısız: " + loginRequest.getEmail());
               return new ErrorDataResult<>("Kullanıcı adı veya şifre hatalı");
           }

           // Set refresh token in the cookie
           int maxAge = (int) (jwtTokenProvider.getRefreshExpiration() / 1000);
           response.addCookie(addCookie("refreshToken", authResponse.getRefreshToken(), maxAge, "/", true, false));

           log.info(messageService.getMessage("user.login.true", new Object[]{user.getEmail()}));
           return new SuccessDataResult<>(
                   authResponse,
                   messageService.getMessage("user.login.true", new Object[]{user.getEmail()})
           );
       }catch(BadCredentialsException e){
            log.warn(messageService.getMessage("user.login.bad.credentials.message", new Object[]{loginRequest.getEmail()}), e);
            return new ErrorDataResult<>(
                    null,
                    messageService.getMessage("user.login.bad.credentials.message", new Object[]{loginRequest.getEmail()})
            );
       }catch (Exception e){
           log.error(messageService.getMessage("user.login.false", null), e);
           return new ErrorDataResult<>(
                   messageService.getMessage("user.login.false", null)
           );
       }
    }
    private Cookie addCookie(String name, String value, int maxAge, String path, boolean httpOnly, boolean secure) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        return cookie;
    }
    private AuthenticationResponse authenticationResponse(User user) {

        if(!user.getIsActive()){
            return AuthenticationResponse.builder()
                    .isActive(false)
                    .email(user.getEmail())
                    .build();
        }
        String accessToken = jwtTokenProvider.generateToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        log.info(messageService.getMessage("token.create.success", new Object[]{user.getUsername()}));
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isActive(user.getIsActive())
                .email(user.getEmail())
                .build();
    }

    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
    public DataResult<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.warn("refreshToken request received");
        try {
            String refreshToken = extractRefreshTokenFromCookies(request);
            if (refreshToken == null) {
                log.error(messageService.getMessage("refresh_token.error", null));
                return new ErrorDataResult<>(
                        null,
                        messageService.getMessage("refresh_token.error", null)
                );
            }

            // Extract user email from the refresh token
            String userEmail = jwtTokenProvider.extractUserEmail(refreshToken);
            if (userEmail == null) {
                String errorMessage = messageService.getMessage("user.not.found.message", new Object[]{null});
                log.error(errorMessage);
                return new ErrorDataResult<>(null, errorMessage);
            }

            // Validate the refresh token
            User user = userRepository.findByEmail(userEmail).orElseThrow(EntityNotFoundException::new);
            if (!jwtTokenProvider.validateToken(refreshToken, user)) {
                return new ErrorDataResult<>(
                        null,
                        messageService.getMessage("refresh_token.error", new Object[]{null})
                );
            }

            // Generate a new access token
            String accessToken = jwtTokenProvider.generateToken(user);
            log.info(messageService.getMessage("refresh_token.success", new Object[]{user.getEmail()}));
            AuthenticationResponse authResponse = AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .build();

            return new SuccessDataResult<>(
                    authResponse,
                    messageService.getMessage("refresh_token.success", new Object[]{user.getEmail()})
            );
        } catch (Exception e) {
            log.error(messageService.getMessage("refresh_token.error", null), e);
            return new ErrorDataResult<>(
                    null,
                    messageService.getMessage("refresh_token.error", null)
            );
        }
    }
    /**
     * 1 saat içinde onaylanması gereken email gönderilecek
     */
    private void sendEmailConfirmation(User user){
        try{
            long emailActivationExpTime = 3600000;
            String token = jwtTokenProvider.generateToken(user, emailActivationExpTime);
            Map<String, Object> model = getStringObjectMap(user.getEmail(), token);
            emailService.sendMail(MailObject.builder()
                            .from(rootMail)
                            .to(Collections.singleton(user.getEmail()))
                            .subject(messageService.getMessage("app.name" , null))
//                        .attachments(attachmentObjects)
                            .build(), "emailActivation.ftl"
                    , model);
            log.info(messageService.getMessage("validation.email.send.success", new Object[]{user.getEmail()}));
        }catch (Exception e){
            log.error(messageService.getMessage("validation.email.send.error", new Object[]{user.getEmail()}), e);
        }
    }

    private Map<String, Object> getStringObjectMap(String email, String token){
        String urlActivation = clientBaseUrl + "/email-activation?token="+ token;
        String resendActivationLink = clientBaseUrl + "/resend-email-activation?email=" + email;
        Map<String, Object> model = new HashMap<>();
        model.put("name", messageService.getMessage("app.name", null));
        model.put("activationLink", urlActivation);
        model.put("resendActivationLink", resendActivationLink);
        model.put("title", messageService.getMessage("welcome.message", null));
        return model;
    }

    public Result activation(String token) {
        if (token == null) {
            return new ErrorResult(
                    messageService.getMessage("token.validate.not.blank.message", null)
            );
        }
        String userEmail = jwtTokenProvider.extractUserEmail(token);
        try{
            if(userEmail == null){
                log.error(messageService.getMessage("token.validate.error", null));
                return new ErrorResult(
                        messageService.getMessage("token.validate.error", null)
                );
            }
            User user = userRepository.findByEmail(userEmail).orElse(null);
            if(user == null || user.getIsActive()){
                log.error(messageService.getMessage("validation.email.activation.error", new Object[]{userEmail}));
                return new ErrorResult(
                        messageService.getMessage("validation.email.activation.error", new Object[]{userEmail})
                );
            }
            if(!jwtTokenProvider.validateToken(token, user)){
                log.error(messageService.getMessage("token.validate.error", null));
                return new ErrorResult(
                        messageService.getMessage("token.validate.error", null)
                );
            }
            user.setIsActive(true);
            Role role = roleRepository.findByName(EntityConstants.baseRoleName);
            assignRoleToUser(user.getId(), role.getId());
            jwtTokenBlackList.addBlackListToken(token);
            return new SuccessResult(
                    messageService.getMessage("email.activation.true", null)
            );
        } catch (Exception e) {
            log.error(messageService.getMessage("validation.email.activation.error", new Object[]{userEmail}), e);
            return new ErrorResult(
                    messageService.getMessage("validation.email.activation.error", null)
            );
        }

    }

    public Result resendActivationEmail(String email) {
        try{
            User user = userRepository.findByEmailAddress(email).orElse(null);
            if(user == null){
                log.error(messageService.getMessage("user.not.found.message", new Object[]{email}));
                return new ErrorResult(
                        messageService.getMessage("user.not.found.message", new Object[]{email})
                );
            }
            if(user.getIsActive()){
                log.error(messageService.getMessage("RE_EMAIL.already.activated.message", new Object[]{email}));
                return new ErrorResult(
                        messageService.getMessage("RE_EMAIL.already.activated.message", new Object[]{email})
                );
            }
            sendEmailConfirmation(user);
            log.info(messageService.getMessage("RE_EMAIL.success", new Object[]{email}));
            return new SuccessResult(
                    messageService.getMessage("RE_EMAIL.success", new Object[]{email})
            );
        } catch (Exception e) {
            log.error(messageService.getMessage("RE_EMAIL.error", new Object[]{email}), e);
            return new ErrorResult(
                    messageService.getMessage("RE_EMAIL.error", new Object[]{email})
            );
        }
    }

    @Transactional
    public void assignRoleToUser(Long userId, Long roleId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Role> roleOptional = roleRepository.findById(roleId);

        if (userOptional.isPresent() && roleOptional.isPresent()) {
            User user = userOptional.get();
            Role role = roleOptional.get();

            if (!user.getRoles().contains(role)) {
                user.getRoles().add(role);
                userRepository.save(user);
            } else {
                System.out.println("User already has this role assigned.");
            }
        } else {
            System.out.println("User or Role not found.");
        }
    }


    private static UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .name(user.getName())
                .lastName(user.getLastName())
                .socialAccounts(user.getSocialAccounts())
                .isActive(user.getIsActive())
                .isDeleted(user.getIsDeleted())
                .build();
    }


    public Result logout(String token, HttpServletResponse response){
        jwtTokenBlackList.addBlackListToken(token);
        Cookie expiredCookie = new Cookie("refreshToken", null);
        expiredCookie.setPath("/");
        expiredCookie.setHttpOnly(true);
        expiredCookie.setMaxAge(0);
        response.addCookie(expiredCookie);
        log.info(messageService.getMessage("user.logout.success", null));
        return new SuccessResult(
                messageService.getMessage("user.logout.success", null)
        );
    }
}
