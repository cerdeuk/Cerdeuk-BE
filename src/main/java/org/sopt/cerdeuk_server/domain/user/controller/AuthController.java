package org.sopt.cerdeuk_server.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.cerdeuk_server.domain.user.dto.LoginSuccessResponse;
import org.sopt.cerdeuk_server.domain.user.service.AuthService;
import org.sopt.cerdeuk_server.global.auth.jwt.JwtUtil;
import org.sopt.cerdeuk_server.global.error.code.SuccessCode;
import org.sopt.cerdeuk_server.global.error.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    @PostMapping(value = "/login")
    public ResponseEntity<SuccessResponse<LoginSuccessResponse>> processLoginRequest(@RequestHeader("userId") Long userId){
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, authService.loginWithJwt(userId)));
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<SuccessResponse<Long>> processRegisterRequest(){
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE, authService.register()));
    }

    @GetMapping(value = "/test")
    public ResponseEntity<SuccessResponse<String>> testRequest(
            @AuthenticationPrincipal Long userId
    ){
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH, "유저: " + userId));
    }
}
