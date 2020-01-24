package pl.pw.ocd.app.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.pw.ocd.app.model.UserDTO;
import pl.pw.ocd.app.service.UserService;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserSignUpValidation {
    @Autowired
    UserService userService;

    private int validateLogin(String login) {
        String regex = "^[a-zA-Z0-9]{3,}$";
        if (login.matches(regex)) {
            if (!userService.existsByLogin(login))
                return 0;
            return 1;
        }
        return 2;
    }

    private boolean passwordsMatch(String pass1, String pass2) {
        return pass1.equals(pass2) && pass1.length() != 0;
    }

    private boolean emailValid(String email) {
        String regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        return email.matches(regex);
    }

    private boolean nameNotEmpty(String name) {
        return !name.isEmpty();
    }

    private boolean surnameNotEmpty(String surname) {
        return !surname.isEmpty();
    }

    public Map<String, Integer> checkUser(UserDTO userDTO) {
        Map<String, Integer> result = new HashMap<>();
        result.put("login", validateLogin(userDTO.getLogin()));
        result.put("password", passwordsMatch(userDTO.getPassword1(), userDTO.getPassword2()) ? 0 : 1);
        result.put("email", emailValid(userDTO.getEmail()) ? 0 : 1);
        result.put("name", nameNotEmpty(userDTO.getName()) ? 0 : 1);
        result.put("surname", surnameNotEmpty(userDTO.getSurname()) ? 0 : 1);
        return result;
    }
}
