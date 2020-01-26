package pl.pw.ocd.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pw.ocd.app.exceptions.ExpiredSessionException;
import pl.pw.ocd.app.exceptions.ForbiddenCookieException;
import pl.pw.ocd.app.model.Note;
import pl.pw.ocd.app.model.NoteDTO;
import pl.pw.ocd.app.model.SessionData;
import pl.pw.ocd.app.model.User;
import pl.pw.ocd.app.service.LoginService;
import pl.pw.ocd.app.service.NoteService;
import pl.pw.ocd.app.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class NoteController {
    @Autowired
    NoteService noteService;

    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

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

    @RequestMapping(value = "/notes", method = RequestMethod.GET)
    public ModelAndView getNotesPage(HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity responseEntity = checkCookies(request, response);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            Cookie[] cookies = request.getCookies();
            Cookie user = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("user")).findAny().orElse(null);
            ModelAndView modelAndView = new ModelAndView("notes");
            List<Note> myNotes = noteService.getByOwner(user.getValue());
            if (null != myNotes && !myNotes.isEmpty())
                modelAndView.addObject("myNotes", myNotes);
            List<Note> otherNotes = noteService.getPermittedNotes(user.getValue());
            if (null != otherNotes && !otherNotes.isEmpty())
                modelAndView.addObject("otherNotes", otherNotes);
            return modelAndView;
        } else {
            return new ModelAndView("unauthorized");
        }
    }

    @RequestMapping(value = "/notes/create", method = RequestMethod.GET)
    public ModelAndView getCreateNotePage(HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity responseEntity = checkCookies(request, response);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            ModelAndView modelAndView = new ModelAndView("createnote");
            modelAndView.addObject("note", new NoteDTO());
            return modelAndView;
        } else {
            return new ModelAndView("unauthorized");
        }
    }

    @RequestMapping(value = "/notes/create", method = RequestMethod.POST)
    public ModelAndView createNote(@ModelAttribute NoteDTO noteDTO, HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity responseEntity = checkCookies(request, response);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            Cookie[] cookies = request.getCookies();
            Cookie user = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("user")).findAny().orElse(null);
            User logged = userService.getUserByLogin(user.getValue());

            Note note = new Note();
            note.setNoteText(noteDTO.getNoteText());
            note.setOwner(logged.getName() + " " + logged.getSurname());
            note.setOwnerLogin(logged.getLogin());
            switch (noteDTO.getType()) {
                case 0:
                    note.setPublic(false);
                    note.setPrivate(true);
                    note.setPermitted(new ArrayList<>());
                    break;

                case 1:
                    note.setPublic(true);
                    note.setPrivate(false);
                    note.setPermitted(new ArrayList<>());
                    break;

                case 2:
                    note.setPublic(false);
                    note.setPrivate(false);
                    List<String> permitted = getSharedLogins(noteDTO.getSharedLogins());
                    if (!permitted.isEmpty())
                        note.setPermitted(getSharedLogins(noteDTO.getSharedLogins()));
                    else {
                        note.setPermitted(new ArrayList<>());
                        note.setPrivate(true);
                    }
                    break;
            }
            noteService.createNote(note);
            ModelAndView modelAndView = new ModelAndView("redirect:/notes");
            return modelAndView;
        } else {
            return new ModelAndView("unauthorized");
        }
    }

    @RequestMapping(value = "/notes/{noteId}/delete", method = RequestMethod.POST)
    public ModelAndView deleteNote(@PathVariable String noteId, HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity responseEntity = checkCookies(request, response);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            noteService.deleteNote(noteId);
            return new ModelAndView("redirect:/notes");
        } else {
            return new ModelAndView("unauthorized");
        }
    }

    private List<String> getSharedLogins(String data) {
        List<String> result = new ArrayList<>();
        String[] listData = data.split(" ");
        for (String s : listData) {
            if (s.matches("^[a-zA-Z0-9]+$") && userService.existsByLogin(s)) {
                result.add(s);
            }
        }
        return result;
    }
}
