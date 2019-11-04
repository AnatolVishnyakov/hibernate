package entities;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;

@Entity
public class Person {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
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
