package org.sopt.certi_server.domain.user.dto;

public record LoginSuccessResponse(
    String accessToken,
    String refreshToken
) {
    public static LoginSuccessResponse of(String accessToken, String refreshToken){
        return new LoginSuccessResponse(accessToken, refreshToken);
    }
}
