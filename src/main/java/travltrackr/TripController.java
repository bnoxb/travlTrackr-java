package travltrackr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
public class TripController {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/api/trips")
    public Trip createTrip(@RequestBody Trip trip, HttpSession session)throws Exception{
        User foundUser = userRepository.findByUsername(session.getAttribute("username").toString());
        if(foundUser == null){
            throw new Exception("You need to login");
        }
        trip.setUser(foundUser);
        return  tripRepository.save(trip);
    }

    @GetMapping("/api/trips")
    public Iterable<Trip> indexTrip(){
        return tripRepository.findAll();
    }

    @GetMapping("/api/trips/{id}")
    public Optional<Trip> showTrip(@PathVariable Long id){
        Optional<Trip> foundTrip = tripRepository.findById(id);
        return foundTrip;
    }

    @GetMapping("/api/users/{userId}/trips")
    public Iterable<Trip> showUsersTrips(@PathVariable Long userId){
        Optional<User> foundUser = userRepository.findById(userId);
        Iterable<Trip> foundTrips = tripRepository.findAllByUser(foundUser);
        return foundTrips;
    }

    @PutMapping("/api/trips/{id}")
    public Trip updateTrip(@PathVariable Long id, @RequestBody Trip trip){
        Optional<Trip> tripToEdit = tripRepository.findById(id);
        Trip foundTrip = tripToEdit.get();
        foundTrip.setName(trip.getName());
        foundTrip.setState(trip.getState());
        foundTrip.setCountry(trip.getCountry());
        foundTrip.setDateArrived(trip.getDateArrived());
        foundTrip.setDateLeft(trip.getDateLeft());
        foundTrip.setNotes(trip.getNotes());
        return tripRepository.save(foundTrip);
    }

    @DeleteMapping("/api/trips/{id}")
    public String deleteTrip(@PathVariable Long id){
        tripRepository.deleteById(id);
        return "Deleted the trip";
    }

    @GetMapping("/api/trips/call")
    public Object apiCall()throws Exception{
        HttpRequest test = new HttpRequest();
        Object content = test.testURL(test.URLSTRING);
        return content;
    }

    @GetMapping("/api/trips/call2")
    public Object apiFetch()throws Exception{
        URLFetcher test = new URLFetcher();
        Object content = test.urlFetch();
        return content;
    }
}
