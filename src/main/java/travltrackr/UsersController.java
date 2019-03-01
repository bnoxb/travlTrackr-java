package travltrackr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/api/users")
    public Iterable<Users> index(){
        return usersRepository.findAll();
    }

    @PostMapping("/api/users/register")
    public Users createUser(@RequestBody Users users){
        Users createdUsers = usersService.saveUsers(users);
        return createdUsers;
    }

    @PostMapping("/api/users/login")
    public Users checkLogin(@RequestBody Users users, HttpSession session)throws Exception{

        bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Users foundUsers = usersRepository.findByUsername(users.getUsername());
        boolean validPass = bCryptPasswordEncoder.matches(users.getPassword(), foundUsers.getPassword());
        if(validPass){
            session.setAttribute("username", foundUsers.getUsername());
            return foundUsers;
        }else{
            throw new Exception("incorrect credentials");
        }
    }

    @GetMapping("/api/users/{id}")
    public Optional<Users> showUser(@PathVariable Long id){
        Optional<Users> foundUser = usersRepository.findById(id);
        return foundUser;
    }

    @PutMapping("/api/users/{id}")
    public Users editUser(@PathVariable Long id, @RequestBody Users users){
        // Dont forget to make a new BCrypt when using bcrypt
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<Users> userToEdit = usersRepository.findById(id);
        Users foundUsers = userToEdit.get();
        foundUsers.setUsername(users.getUsername());
        foundUsers.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
        foundUsers.setEmail(users.getEmail());
        return usersRepository.save(foundUsers);
    }

    @DeleteMapping("/api/users/{id}")
    public String deleteUser(@PathVariable Long id){
        usersRepository.deleteById(id);
        return "Deleted user with user id: " + id;
    }
}
