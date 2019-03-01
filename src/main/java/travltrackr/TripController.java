package travltrackr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@RestController
public class TripController {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Value("${yelp.api.client_secret}")
    private String yelpApiKey;

    @PostMapping("/api/trips")
    public Trip createTrip(@RequestBody HashMap<String, Object> trip, HttpSession session)throws Exception{
        Users foundUsers = usersRepository.findByUsername(session.getAttribute("username").toString());
        if(foundUsers == null){
            throw new Exception("You need to login");
        }
        System.out.println("------------------------------");
        System.out.println(trip.get("initialNote"));
        System.out.println(trip.get("name"));
        Trip tripToSave = new Trip();
        tripToSave.setName(trip.get("name").toString());
        tripToSave.setState(trip.get("state").toString());
        tripToSave.setCountry(trip.get("country").toString());
        // convert the date object into a date
        Date dateArrived = new SimpleDateFormat("yyyy-MM-dd").parse(trip.get("dateArrived").toString());
        Date dateLeft = new SimpleDateFormat("yyyy-MM-dd").parse(trip.get("dateLeft").toString());
        tripToSave.setDateArrived(dateArrived);
        tripToSave.setDateLeft(dateLeft);
        tripToSave.setUsers(foundUsers);
        Trip createdTrip = tripRepository.save(tripToSave);
        Note note = new Note();
        note.setNotes(trip.get("initialNote").toString());
        note.setTrip(createdTrip);
        noteRepository.save(note);
        return createdTrip;
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
        Optional<Users> foundUser = usersRepository.findById(userId);
        Iterable<Trip> foundTrips = tripRepository.findAllByUsers(foundUser);
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
        return tripRepository.save(foundTrip);
    }

    @DeleteMapping("/api/trips/{id}")
    public String deleteTrip(@PathVariable Long id){
        tripRepository.deleteById(id);
        return "Deleted the trip";
    }

    @GetMapping("/api/trips/call/{location}/{attr}")
    public Object apiCall(@PathVariable String location, @PathVariable String attr)throws Exception{
        String finalAttr = "";
        if(attr == "hot_and_new"){
            finalAttr = "&attributes=hot_and_new";
        }else if(attr == "most_reviewed"){
            finalAttr = "&sort_by=review_count";
        }else if(attr == "best_match"){
            finalAttr = "&sort_by=best_match";
        }
        HttpRequest test = new HttpRequest();
        Object content = test.testURL(test.URLCOMPOUND, yelpApiKey, location, finalAttr);
        return content;
    }

   @GetMapping("/api/trips/{id}/notes")
    public Iterable<Note> getNotes(@PathVariable Long id){
        Optional<Trip> foundTrip = tripRepository.findById(id);
        return noteRepository.findAllByTrip(foundTrip);
   }

   @DeleteMapping("/api/notes/{id}")
    public String deleteNote(@PathVariable Long id){
        noteRepository.deleteById(id);
        return "Deleted the note with id: " + id;
   }

   @PostMapping("/api/trips/{tripId}/notesAdd")
    public Note addNote(@PathVariable Long tripId, @RequestBody String note)throws Exception{
        Optional<Trip> foundTrip = tripRepository.findById(tripId);
        if(foundTrip.isPresent()){
            Note newNote = new Note();
            newNote.setNotes(note);
            newNote.setTrip(foundTrip.get());
            return noteRepository.save(newNote);
        }else{
            throw new Exception("There is no trip with that id");
        }
   }
}
