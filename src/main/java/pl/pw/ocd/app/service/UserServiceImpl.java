package pl.pw.ocd.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pw.ocd.app.model.User;
import pl.pw.ocd.app.repositories.UserRepository;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean existsByLogin(String login) {
        return userRepository.existsById(login);
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findById(login).orElse(null);
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void incrementBadLogin(String login) {
        User user = getUserByLogin(login);
        deleteUserByLogin(login);
        user.setBadLogins(user.getBadLogins() + 1);
        createUser(user);
    }

    @Override
    public void attachIpAdress(String login, String ipAdress) {
        User user = getUserByLogin(login);
        deleteUserByLogin(login);
        if (null == user.getAdresses())
            user.setAdresses(new ArrayList<>());
        if (user.getAdresses().isEmpty() || !user.getAdresses().contains(ipAdress)) {
            user.getAdresses().add(ipAdress);
        }
        createUser(user);
    }

    @Override
    public void changePassword(String login, String password) {
        User user = getUserByLogin(login);
        deleteUserByLogin(login);
        user.setPassword(password);
        user.setAdresses(new ArrayList<>());
        user.setBadLogins(0);
        createUser(user);
    }

    @Override
    public void deleteUserByLogin(String login) {
        User userToDelete = getUserByLogin(login);
        if (null != userToDelete) {
            userRepository.delete(userToDelete);
        }
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}
