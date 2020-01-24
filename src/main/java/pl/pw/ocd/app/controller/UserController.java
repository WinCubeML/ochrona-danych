package pl.pw.ocd.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pw.ocd.app.logic.UserSignUpValidation;
import pl.pw.ocd.app.model.UserDTO;
import pl.pw.ocd.app.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserSignUpValidation userSignUpValidation;

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
            // TODO stworzyć haszowanie hasła, zeby potem zapisać użytkownika w bazie
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
}
