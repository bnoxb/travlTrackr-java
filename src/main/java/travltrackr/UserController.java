package travltrackr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/api/users")
    public Iterable<User> index(){
        return userRepository.findAll();
    }

    @PostMapping("/api/users/register")
    public User createUser(@RequestBody User user){
        User createdUser = userService.saveUser(user);
        return createdUser;
    }

    @PostMapping("/api/users/login")
    public User checkLogin(@RequestBody User user, HttpSession session)throws Exception{

        bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User foundUser = userRepository.findByUsername(user.getUsername());
        boolean validPass = bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getPassword());
        if(validPass){
            session.setAttribute("username", foundUser.getUsername());
            return foundUser;
        }else{
            throw new Exception("incorrect credentials");
        }
    }

    @GetMapping("/api/users/{id}")
    public Optional<User> showUser(@PathVariable Long id){
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser;
    }

    @PutMapping("/api/users/{id}")
    public User editUser(@PathVariable Long id, @RequestBody User user){
        // Dont forget to make a new BCrypt when using bcrypt
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<User> userToEdit = userRepository.findById(id);
        User foundUser = userToEdit.get();
        foundUser.setUsername(user.getUsername());
        foundUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        foundUser.setEmail(user.getEmail());
        return userRepository.save(foundUser);
    }

    @DeleteMapping("/api/users/{id}")
    public String deleteUser(@PathVariable Long id){
        userRepository.deleteById(id);
        return "Deleted user with user id: " + id;
    }
}
