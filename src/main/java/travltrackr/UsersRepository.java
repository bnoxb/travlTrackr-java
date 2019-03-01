package travltrackr;


import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Long> {
    public Users findByUsername(String username);
}
