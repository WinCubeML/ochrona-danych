package pl.pw.ocd.app.service;

import org.springframework.stereotype.Service;
import pl.pw.ocd.app.model.Note;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    @Override
    public void createNote(Note note) {

    }

    @Override
    public Note getNote(String noteId) {
        return null;
    }

    @Override
    public List<Note> getPermittedNotes(String login) {
        return null;
    }

    @Override
    public List<Note> getAllNotes() {
        return null;
    }

    @Override
    public Note modifyNote(Note oldNote, Note newNote) {
        return null;
    }

    @Override
    public Note modifyPermitted(Note note, String logins) {
        return null;
    }

    @Override
    public void deleteNote(String noteId) {

    }
}
