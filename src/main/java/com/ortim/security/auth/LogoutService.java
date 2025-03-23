package com.ortim.security.auth;

import com.ortim.component.MessageService;
import com.ortim.core.exception.ErrorCode;
import com.ortim.core.exception.GenericException;
import com.ortim.security.jwt.JwtTokenBlackList;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final JwtTokenBlackList jwtTokenBlackList;

    private final MessageService messageService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        if(jwt.isBlank()){
            throw GenericException.builder()
                    .errorMessage(messageService.getMessage("user.logout.null.token", null))
                    .errorCode(ErrorCode.BAD_REQUEST)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        jwtTokenBlackList.addBlackListToken(jwt);
        SecurityContextHolder.clearContext();

        // Clear the refresh token cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    String refreshToken = cookie.getValue();
                    if (refreshToken != null && !refreshToken.isBlank()) {
                        jwtTokenBlackList.addBlackListToken(refreshToken);
                    }
                    cookie.setMaxAge(0);
                    cookie.setValue(null);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                }
            }
        }


        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        try {
            response.getWriter().write("{\"message\": \"Logout successful\",");
            response.getWriter().write("\"success\" : true }");
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
