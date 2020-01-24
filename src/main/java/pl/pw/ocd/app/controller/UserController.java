package pl.pw.ocd.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pw.ocd.app.model.User;
import pl.pw.ocd.app.service.UserService;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signUpUser() {
        ModelAndView modelAndView = new ModelAndView("signup");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }
}
