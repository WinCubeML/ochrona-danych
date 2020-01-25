package pl.pw.ocd.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pw.ocd.app.logic.PasswordEncoder;
import pl.pw.ocd.app.model.LoginDTO;
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

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLoginPage() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginDTO", new LoginDTO());
        return modelAndView;
    }
}
