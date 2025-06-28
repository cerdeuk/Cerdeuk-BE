package org.sopt.cerdeuk_server.global.jwt.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@RedisHash(value = "refresh_token")
public class Token {

	@Id
	private Long id;

	@Indexed
	private String refreshToken;

	public static Token of(final Long id, final String refreshToken) {
		return Token.builder()
			.id(id)
			.refreshToken(refreshToken)
			.build();
	}
}
