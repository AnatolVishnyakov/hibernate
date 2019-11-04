package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Person {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToMany
    private List<HomeAddress> address;

    public Person(String name, List<HomeAddress> address) {
        this.name = name;
        this.address = address;
    }

    public Person() {

    }
}
