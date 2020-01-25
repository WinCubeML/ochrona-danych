package pl.pw.ocd.app.service;

import org.springframework.stereotype.Service;
import pl.pw.ocd.app.model.SessionData;

import java.util.List;

@Service
public interface LoginService {
    SessionData createSession(SessionData sessionData);

    SessionData getSessionById(String sessionId);

    List<SessionData> getAllSessions();

    void destroySession(SessionData sessionData);

    void checkExpiredSessions();
}
