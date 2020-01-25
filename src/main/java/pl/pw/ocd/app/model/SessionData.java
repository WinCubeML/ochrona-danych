package pl.pw.ocd.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("session")
@Data
@NoArgsConstructor
public class SessionData {
    @Id
    private String sessionId;

    private String login;

    private String expiryDate;
}
