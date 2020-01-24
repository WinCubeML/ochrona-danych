package pl.pw.ocd.app.service;

import org.springframework.stereotype.Service;
import pl.pw.ocd.app.model.User;

@Service
public interface UserService {
    void createUser(User user);

    User getUserByLogin(String login);

    Iterable<User> getAllUsers();

    void deleteUserByLogin(String login);

    void deleteAllUsers();
}
