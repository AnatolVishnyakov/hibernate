package entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class HomeAddress {
    @Id
    @GeneratedValue
    private int id;
    private String street;
    @ManyToMany
    private List<Person> person;

    public HomeAddress(String street) {
        this.street = street;
    }

    public HomeAddress() {
    }

    public void setPerson(List<Person> person) {
        this.person = person;
    }
}
