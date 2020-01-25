package pl.pw.ocd.app.service;

import org.springframework.stereotype.Service;
import pl.pw.ocd.app.model.Note;

import java.util.List;

@Service
public interface NoteService {
    void createNote(Note note);

    Note getNote(String noteId);

    List<Note> getPermittedNotes(String login);

    List<Note> getAllNotes();

    Note modifyNote(Note oldNote, Note newNote);

    Note modifyPermitted(Note note, String logins);

    void deleteNote(String noteId);
}
