package travltrackr;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Trip {
    private String name;
    private String state;
    private String country;
    private Date dateArrived;
    private Date dateLeft;


    @ManyToOne
    @JoinColumn(
            name="user_id"
    )
    private User user;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private Set<Note> notes;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getDateArrived() {
        return dateArrived;
    }

    public void setDateArrived(Date dateArrived) {
        this.dateArrived = dateArrived;
    }

    public Date getDateLeft() {
        return dateLeft;
    }

    public void setDateLeft(Date dateLeft) {
        this.dateLeft = dateLeft;
    }

    public Set getNote() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
