package pl.pw.ocd.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pw.ocd.app.model.SessionData;
import pl.pw.ocd.app.repositories.LoginRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    private LoginRepository loginRepository;

    @Autowired
    public LoginServiceImpl(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public SessionData createSession(SessionData sessionData) {
        return loginRepository.save(sessionData);
    }

    @Override
    public SessionData getSessionById(String sessionId) {
        return loginRepository.findById(sessionId).orElse(null);
    }

    @Override
    public List<SessionData> getAllSessions() {
        return (List<SessionData>) loginRepository.findAll();
    }

    @Override
    public void destroySession(SessionData sessionData) {
        loginRepository.delete(sessionData);
    }

    @Override
    public void checkExpiredSessions() {
        List<SessionData> sessions = new ArrayList<>();
        loginRepository.findAll().forEach(sessions::add);
        for (SessionData session : sessions) {
            if (null != session) {
                LocalDateTime dateTime = LocalDateTime.parse(session.getExpiryDate());
                if (LocalDateTime.now().isAfter(dateTime)) {
                    destroySession(session);
                }
            }
        }
    }
}
