package pl.pw.ocd.app.service;

import org.springframework.stereotype.Service;
import pl.pw.ocd.app.model.Note;
import pl.pw.ocd.app.repositories.NoteRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private NoteRepository noteRepository;

    @Override
    public void createNote(Note note) {
        noteRepository.save(note);
    }

    @Override
    public Note getNote(String noteId) {
        return noteRepository.findById(noteId).orElse(null);
    }

    @Override
    public List<Note> getPermittedNotes(String login) {
        List<Note> notes = getAllNotes();
        notes = notes.stream().filter(note -> note.getPermitted().contains(login)).collect(Collectors.toList());
        return notes;
    }

    @Override
    public List<Note> getAllNotes() {
        return (List<Note>) noteRepository.findAll();
    }

    @Override
    public void modifyNote(Note oldNote, Note newNote) {
        newNote.setNoteId(oldNote.getNoteId());
        deleteNote(oldNote);
        createNote(newNote);
    }

    @Override
    public void setNotePrivate(String noteId) {
        Note note = getNote(noteId);
        deleteNote(note);
        note.setPublic(false);
        note.setPrivate(true);
        note.setPermitted(new ArrayList<>());
        createNote(note);
    }

    @Override
    public void setNotePublic(String noteId) {
        Note note = getNote(noteId);
        deleteNote(note);
        note.setPublic(true);
        note.setPrivate(false);
        note.setPermitted(new ArrayList<>());
        createNote(note);
    }

    @Override
    public void modifyPermitted(String noteId, String logins) {
        String[] loginToList = logins.split(" ");
        List<String> loginArray = Arrays.asList(loginToList);
        Note note = getNote(noteId);
        note.setPrivate(false);
        note.setPublic(false);
        deleteNote(note);
        note.setPermitted(loginArray);
        createNote(note);
    }

    @Override
    public void deleteNote(String noteId) {
        deleteNote(getNote(noteId));
    }

    @Override
    public void deleteNote(Note note) {
        noteRepository.delete(note);
    }

    @Override
    public void deleteAllNotes() {
        noteRepository.deleteAll();
    }
}
