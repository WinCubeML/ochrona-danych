package pl.pw.ocd.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pw.ocd.app.model.User;
import pl.pw.ocd.app.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public void createUser(User user) {
        userRepository.save(user);
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
