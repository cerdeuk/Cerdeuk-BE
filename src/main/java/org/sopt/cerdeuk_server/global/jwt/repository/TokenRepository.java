package org.sopt.cerdeuk_server.global.jwt.repository;

import java.util.Optional;

import org.sopt.cerdeuk_server.global.jwt.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
	Optional<Token> findByRefreshToken(String refreshToken);
}
