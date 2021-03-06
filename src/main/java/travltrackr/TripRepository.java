package travltrackr;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TripRepository extends CrudRepository<Trip, Long>{
    public Iterable<Trip> findAllByUsers(Optional<Users> users);
}
