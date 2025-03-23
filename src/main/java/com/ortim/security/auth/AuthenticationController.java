package com.ortim.security.auth;

import com.ortim.core.results.DataResult;
import com.ortim.core.results.Result;
import com.ortim.core.utils.RouteConstants;
import com.ortim.security.request.LoginRequest;
import com.ortim.security.request.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3001", allowedHeaders = "*", allowCredentials = "true")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping(RouteConstants.authRegisterRoute)
    public ResponseEntity<DataResult<?>> register(
           @Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(service.register(request, response));
    }
    @PostMapping(RouteConstants.authLoginRoute)
    public ResponseEntity<DataResult<?>> authenticate(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(service.login(request, response));
    }
    @PostMapping(RouteConstants.authActivationRoute)
    public ResponseEntity<?> emailActivation(@Valid @RequestParam Map<String, String> request){
        String token = request.get("token");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "token parametresi gereklidir."
                    )
            );
        }
        return ResponseEntity.ok(service.activation(token));
    }
    @PostMapping(RouteConstants.authResendActivationRoute)
    public ResponseEntity<?> resendEmailActivation(@Valid @RequestBody Map<String, String> request){
        String email = request.get("email");
        log.info("Resend Email => {}", email);
        if (email == null || email.isEmpty()) {
            log.error("Email resend error => {}", email);
            return ResponseEntity.badRequest().body(
                    Map.of("success", false,
                            "message", "E-posta adresi gereklidir.")
            );
        }
        log.info("Resend Email OK => {}", email);
        return ResponseEntity.ok(service.resendActivationEmail(email));
    }

    @PostMapping(RouteConstants.authRefreshTokenRoute)
    public ResponseEntity<Result> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        return ResponseEntity.ok(service.refreshToken(request, response));
    }
}
