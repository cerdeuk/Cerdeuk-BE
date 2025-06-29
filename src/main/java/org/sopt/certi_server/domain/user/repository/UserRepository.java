package org.sopt.certi_server.domain.user.repository;

import org.sopt.certi_server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
