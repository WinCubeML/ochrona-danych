package pl.pw.ocd.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pw.ocd.app.exceptions.ExpiredSessionException;
import pl.pw.ocd.app.exceptions.ForbiddenCookieException;
import pl.pw.ocd.app.logic.PasswordEncoder;
import pl.pw.ocd.app.logic.UserSignUpValidation;
import pl.pw.ocd.app.model.ChangePasswordDTO;
import pl.pw.ocd.app.model.SessionData;
import pl.pw.ocd.app.model.User;
import pl.pw.ocd.app.model.UserDTO;
import pl.pw.ocd.app.service.LoginService;
import pl.pw.ocd.app.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    @Autowired
    UserSignUpValidation userSignUpValidation;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signUpUserPage() {
        ModelAndView modelAndView = new ModelAndView("signup");
        modelAndView.addObject("user", new UserDTO());
        modelAndView.addObject("errors", new ArrayList<String>());
        return modelAndView;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    protected ModelAndView signUpUser(@ModelAttribute UserDTO userDTO) {
        boolean valid = true;
        List<String> errors = new ArrayList<>();
        Map<String, Integer> checkUser = userSignUpValidation.checkUser(userDTO);
        for (Map.Entry<String, Integer> entry : checkUser.entrySet()) {
            if (entry.getValue() != 0) {
                valid = false;
                switch (entry.getKey()) {
                    case "login":
                        if (entry.getValue() == 1) {
                            errors.add("Podany login jest zajęty. Proszę wpisać inny.");
                        } else {
                            errors.add("Login może się składać tylko z małych i dużych liter alfabetu angielskiego i cyfer");
                        }
                        break;

                    case "password":
                        errors.add("Hasła nie mogą być puste oraz muszą być tożsame w obu rubrykach.");
                        break;

                    case "email":
                        errors.add("Podany email ma nieprawidłowy format.");
                        break;

                    case "name":
                        errors.add("Imię nie może być puste.");
                        break;

                    case "surname":
                        errors.add("Nazwisko nie może być puste.");
                        break;
                }
            }
        }

        if (valid) {
            ModelAndView modelAndView = new ModelAndView("redirect:/");
            User user = new User();
            user.setLogin(userDTO.getLogin());
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getName());
            user.setSurname(userDTO.getSurname());
            user.setPassword(passwordEncoder.hashPassword(userDTO.getPassword1()));
            userService.createUser(user);
            modelAndView.setStatus(HttpStatus.CREATED);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("signup");
            modelAndView.addObject("user", userDTO);
            modelAndView.addObject("errors", errors);
            modelAndView.setStatus(HttpStatus.BAD_REQUEST);
            return modelAndView;
        }
    }

    @RequestMapping(value = "/changepass", method = RequestMethod.GET)
    public ModelAndView getChangePasswordPage(HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity responseEntity = checkCookies(request, response);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            ModelAndView modelAndView = new ModelAndView("changepassword");
            modelAndView.addObject("pass", new ChangePasswordDTO());
            modelAndView.addObject("errors", new ArrayList<>());
            return modelAndView;
        } else {
            return new ModelAndView("unauthorized");
        }
    }

    @RequestMapping(value = "/changepass", method = RequestMethod.POST)
    public ModelAndView changePassword(@ModelAttribute ChangePasswordDTO passwordDTO, HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity responseEntity = checkCookies(request, response);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            Cookie[] cookies = request.getCookies();
            Cookie user = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("user")).findAny().orElse(null);

            boolean goodOld = validateUser(user.getValue(), passwordDTO.getOld());
            boolean goodNew = !passwordDTO.getNewpass().equals("") && passwordDTO.getNewpass().equals(passwordDTO.getRepeatpass());
            boolean theseAreTheSame = passwordDTO.getOld().equals(passwordDTO.getNewpass()) && goodNew;

            if (goodOld && goodNew && !theseAreTheSame) {
                userService.changePassword(user.getValue(), passwordEncoder.hashPassword(passwordDTO.getNewpass()));
                return new ModelAndView("redirect:/notes");
            } else {
                List<String> errors = new ArrayList<>();
                if (!goodOld) {
                    errors.add("Aktualne hasło nie jest prawidłowe.");
                }
                if (!goodNew) {
                    errors.add("Nowe hasła nie są tożsame.");
                }
                if (theseAreTheSame) {
                    errors.add("Nie możesz zmienić hasła na takie, jakie było teraz.");
                }
                ModelAndView modelAndView = new ModelAndView("changepassword");
                modelAndView.addObject("pass", new ChangePasswordDTO());
                modelAndView.addObject("errors", errors);
                return modelAndView;
            }
        } else {
            return new ModelAndView("unauthorized");
        }
    }

    private boolean validateUser(String login, String password) {
        User user = userService.getUserByLogin(login);
        if (null == user)
            return false;
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        return b.matches(password, user.getPassword());
    }

    ResponseEntity checkCookies(HttpServletRequest request, HttpServletResponse response) {
        loginService.checkExpiredSessions();
        try {
            Cookie[] cookies = request.getCookies();
            if (null == cookies) {
                throw new ExpiredSessionException();
            }
            Cookie session = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("sessionid")).findAny().orElse(null);
            Cookie user = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("user")).findAny().orElse(null);
            if (null == session || null == user) {
                System.out.println("Sesja wygasła");
                throw new ExpiredSessionException();
            }

            SessionData checkSession = loginService.getSessionById(session.getValue());
            if (null == checkSession || !checkSession.getLogin().equals(user.getValue())) {
                System.out.println("Znaleziono zakazane ciasteczko");
                if (null != checkSession)
                    loginService.destroySession(checkSession);
                session.setMaxAge(0);
                user.setMaxAge(0);
                response.addCookie(session);
                response.addCookie(user);
                throw new ForbiddenCookieException();
            }
        } catch (ExpiredSessionException | ForbiddenCookieException e) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
