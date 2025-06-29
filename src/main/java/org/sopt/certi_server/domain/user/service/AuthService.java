package org.sopt.certi_server.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.user.domain.User;
import org.sopt.certi_server.domain.user.dto.LoginSuccessResponse;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.sopt.certi_server.global.jwt.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Long register(){
        User user = User.createUser();
        userRepository.save(user);
        return user.getId();
    }

    public LoginSuccessResponse loginWithJwt(Long userId){
        User findUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        String accessToken = jwtUtil.createAccessToken(userId);
        String refreshToken = jwtUtil.createRefreshToken(userId);
        return LoginSuccessResponse.of(accessToken, refreshToken);
    }
}
