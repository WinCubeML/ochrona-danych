package pl.pw.ocd.app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pw.ocd.app.model.Note;

@Repository
public interface NoteRepository extends CrudRepository<Note, String> {
}
