package com.hodol.toy_hodol.domain.auth.repository;

import com.hodol.toy_hodol.domain.auth.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByAccessToken(String accessToken);

}
