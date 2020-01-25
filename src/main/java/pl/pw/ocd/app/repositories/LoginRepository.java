package pl.pw.ocd.app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pw.ocd.app.model.SessionData;

@Repository
public interface LoginRepository extends CrudRepository<SessionData, String> {
}
