package entities;

import javax.persistence.*;

@Entity
public class Person {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToOne
    private HomeAddress address;

    public Person(String name, HomeAddress address) {
        this.name = name;
        this.address = address;
    }

    public Person() {

    }

    public void setAddress(HomeAddress address) {
        this.address = address;
    }
}
