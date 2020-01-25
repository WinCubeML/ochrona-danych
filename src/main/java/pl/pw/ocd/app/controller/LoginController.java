package pl.pw.ocd.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.pw.ocd.app.logic.PasswordEncoder;
import pl.pw.ocd.app.service.LoginService;
import pl.pw.ocd.app.service.UserService;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    @Autowired
    PasswordEncoder passwordEncoder;
}
