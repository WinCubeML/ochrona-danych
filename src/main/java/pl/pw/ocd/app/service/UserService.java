package pl.pw.ocd.app.service;

import org.springframework.stereotype.Service;
import pl.pw.ocd.app.model.User;

@Service
public interface UserService {
    void createUser(User user);

    boolean existsByLogin(String login);

    User getUserByLogin(String login);

    Iterable<User> getAllUsers();

    void incrementBadLogin(String login);

    void attachIpAdress(String login, String ipAdress);

    void changePassword(String login, String password);

    void deleteUserByLogin(String login);

    void deleteAllUsers();
}
