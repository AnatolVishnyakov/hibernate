package entities;

import javax.persistence.*;

@Entity
public class Student {
    @Id
    @GeneratedValue
    private int id;

    @Basic(optional = false, fetch = FetchType.LAZY)
    @Column(name = "StudentName", unique = true, nullable = false)
    private String name;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
