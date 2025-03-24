package org.yagodka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yagodka.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
