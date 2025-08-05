package com.hodol.toy_hodol.domain.user.repository;

import com.hodol.toy_hodol.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
