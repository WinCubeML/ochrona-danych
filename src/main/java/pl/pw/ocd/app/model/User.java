package pl.pw.ocd.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@RedisHash("user")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    private String login;

    private String password;

    private String email;

    private String name;

    private String surname;

    private int badLogins = 0;

    private List<String> adresses;
}
