package pl.pw.ocd.app.service;

import org.springframework.stereotype.Service;
import pl.pw.ocd.app.model.Note;

import java.util.List;

@Service
public interface NoteService {
    void createNote(Note note);

    Note getNote(String noteId);

    List<Note> getByOwner(String owner);

    List<Note> getPermittedNotes(String login);

    List<Note> getAllNotes();

    void modifyNote(Note oldNote, Note newNote);

    void setNotePrivate(String noteId);

    void setNotePublic(String noteId);

    void modifyPermitted(String noteId, String logins);

    void deleteNote(String noteId);

    void deleteNote(Note note);

    void deleteAllNotes();
}
