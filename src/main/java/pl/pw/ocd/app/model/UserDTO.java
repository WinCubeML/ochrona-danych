package pl.pw.ocd.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private String login;

    private String password;

    private String email;

    private String name;

    private String surname;
}
