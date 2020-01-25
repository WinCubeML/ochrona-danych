package pl.pw.ocd.app.logic;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
    public String hashPassword(String password) {
        String hash = password;
        for (int i = 0; i < 10; i++) {
            hash = hashLoop(hash);
        }
        return hash;
    }

    private String hashLoop(String hash) {
        org.springframework.security.crypto.password.PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(hash);
    }
}
