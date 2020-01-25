package pl.pw.ocd.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.pw.ocd.app.service.LoginService;
import pl.pw.ocd.app.service.NoteService;
import pl.pw.ocd.app.service.UserService;

@Controller
public class NoteController {
    @Autowired
    NoteService noteService;

    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;
}
