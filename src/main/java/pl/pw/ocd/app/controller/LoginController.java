package pl.pw.ocd.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pw.ocd.app.logic.IdCreator;
import pl.pw.ocd.app.logic.PasswordEncoder;
import pl.pw.ocd.app.model.LoginDTO;
import pl.pw.ocd.app.model.SessionData;
import pl.pw.ocd.app.model.User;
import pl.pw.ocd.app.service.LoginService;
import pl.pw.ocd.app.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLoginPage(HttpServletRequest request, HttpServletResponse response) {
        loginService.checkExpiredSessions();
        try {
            Cookie[] cookies = request.getCookies();
            Cookie session = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("sessionid")).findAny().orElse(null);
            Cookie user = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("user")).findAny().orElse(null);
            if (null == session || null == user) {
                System.out.println("Ciasteczka nie znaleziono");
                throw new IllegalStateException();
            }

            SessionData checkSession = loginService.getSessionById(session.getValue());
            if (null != checkSession && checkSession.getLogin().equals(user.getValue())) {
                System.out.println("Ciasteczko znalezione w Redis");
                return new ModelAndView("redirect:/notes");
            } else {
                System.out.println("Ciasteczka nie znaleziono w Redis lub login nie pokrywa się z danymi sesji");
                if (null != checkSession)
                    loginService.destroySession(checkSession);
                session.setMaxAge(0);
                user.setMaxAge(0);
                response.addCookie(session);
                response.addCookie(user);
                throw new IllegalStateException();
            }
        } catch (IllegalStateException | NullPointerException e) {
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("loginDTO", new LoginDTO());
            modelAndView.addObject("error", "");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@ModelAttribute LoginDTO loginDTO, HttpServletResponse response, HttpServletRequest request) throws InterruptedException {
        loginService.checkExpiredSessions();
        Thread.sleep(5000);
        if (!loginDTO.getLogin().matches("^[a-zA-Z0-9]+$") || !userService.existsByLogin(loginDTO.getLogin()) || !validateUser(loginDTO)) {
            if (userService.existsByLogin(loginDTO.getLogin()))
                userService.incrementBadLogin(loginDTO.getLogin());
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("loginDTO", new LoginDTO());
            modelAndView.addObject("error", "y");
            return modelAndView;
        }
        int cookieMaxAge = 5 * 60;

        Cookie userCookie = new Cookie("user", loginDTO.getLogin());
        userCookie.setMaxAge(cookieMaxAge);
        userCookie.setHttpOnly(true);
        userCookie.setSecure(true);
        userCookie.setPath("/");
        response.addCookie(userCookie);

        String sessionId = IdCreator.createId(24);
        Cookie sessionCookie = new Cookie("sessionid", sessionId);
        sessionCookie.setMaxAge(cookieMaxAge);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setSecure(true);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);

        SessionData sessionDataDTO = new SessionData();
        sessionDataDTO.setLogin(loginDTO.getLogin());
        sessionDataDTO.setSessionId(sessionId);
        sessionDataDTO.setExpiryDate(LocalDateTime.now().plusSeconds(cookieMaxAge).toString());
        loginService.createSession(sessionDataDTO);

        System.out.println("Zalogowano");
        return new ModelAndView("redirect:/notes");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        loginService.checkExpiredSessions();
        try {
            Cookie[] cookies = request.getCookies();
            Cookie session = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("sessionid")).findAny().orElse(null);
            Cookie user = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("user")).findAny().orElse(null);
            if (null == session || null == user) {
                throw new IllegalStateException();
            }

            SessionData checkSession = loginService.getSessionById(session.getValue());
            if (null != checkSession)
                loginService.destroySession(checkSession);
            session.setMaxAge(0);
            user.setMaxAge(0);
            response.addCookie(session);
            response.addCookie(user);

        } catch (IllegalStateException | NullPointerException e) {
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:/");
    }

    private boolean validateUser(LoginDTO loginDTO) {
        User user = userService.getUserByLogin(loginDTO.getLogin());
        if (null == user)
            return false;
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        return b.matches(loginDTO.getPassword(), user.getPassword());
    }
}
