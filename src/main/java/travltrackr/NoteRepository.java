package travltrackr;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NoteRepository extends CrudRepository<Note, Long> {
    Iterable<Note> findAllByTrip(Optional<Trip> trip);
}
