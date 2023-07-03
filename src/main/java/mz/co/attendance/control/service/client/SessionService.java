package mz.co.attendance.control.service.client;


import mz.co.attendance.control.dao.entities.ussd.UssdSession;
import mz.co.attendance.control.dao.repositories.UssdSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final UssdSessionRepository ussdSessionRepository;

    public SessionService(UssdSessionRepository ussdSessionRepository) {
        this.ussdSessionRepository = ussdSessionRepository;
    }

    public UssdSession createUssdSession(UssdSession session) {
        return ussdSessionRepository.save(session);
    }

    @Cacheable(cacheNames = {"sessionCache"}, key = "#id", unless = "#result == null ")
    public UssdSession get(String id) {
        return ussdSessionRepository.findUssdSessionBySessionId(id).orElse(null);
    }

    @CachePut(cacheNames = {"sessionCache"}, key = "#session.sessionId")
    public UssdSession update(UssdSession session) {
        if (ussdSessionRepository.existsBySessionId(session.getSessionId())) {
            return ussdSessionRepository.save(session);
        }
        throw new IllegalArgumentException("Invalid Operation");
    }

    @CacheEvict(cacheNames = {"sessionCache"}, key = "#id")
    public void delete(String id) {
        // deleting the session
        ussdSessionRepository.deleteUssdSessionBySessionId(id);
        log.info("Session {0} invalidated", id);
    }

}
