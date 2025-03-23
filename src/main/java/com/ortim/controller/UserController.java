package com.ortim.controller;

import com.ortim.ServiceInterface.FileEntityServiceInterface;
import com.ortim.ServiceInterface.UserServiceInterface;
import com.ortim.core.results.DataResult;
import com.ortim.core.utils.RouteConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserServiceInterface userService;
    private final FileEntityServiceInterface fileEntityService;

//    @GetMapping(RouteConstants.userProfileRoute)
//    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal OAuth2User oAuth2User){
//        return oAuth2User.getAttributes();
//    }
    @GetMapping(RouteConstants.userProfileRoute)
    public ResponseEntity<DataResult<?>> getCurrentUser(){
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping(RouteConstants.userCurrentUserRoute)
    public ResponseEntity<?> getUserBase(){
        return ResponseEntity.ok(userService.getCurrentUser());
    }



    @GetMapping(RouteConstants.publicHomeRoute)
    public ResponseEntity<?> homePage(){
        Map<String, String> message = new HashMap<>();
        message.put("title", "Uygulama Giriş sayfası");
        return ResponseEntity.ok(message);
    }
}
