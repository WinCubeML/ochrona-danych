package pl.pw.ocd.app.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pw.ocd.app.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
