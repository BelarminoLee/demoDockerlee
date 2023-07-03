package mz.co.attendance.control.dao.repositories;

import mz.co.attendance.control.dao.entities.ussd.UssdSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UssdSessionRepository extends JpaRepository<UssdSession, String> {

    Optional<UssdSession> findUssdSessionBySessionId(String sessionId);

    boolean existsBySessionId(String sessionId);

    long deleteUssdSessionBySessionId(String sessionId);


}