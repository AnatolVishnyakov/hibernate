package entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Person {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "personId"),
            inverseJoinColumns = @JoinColumn(name = "homeId")
    )
    private List<HomeAddress> address;

    public Person(String name, List<HomeAddress> address) {
        this.name = name;
        this.address = address;
    }

    public Person() {

    }
}
