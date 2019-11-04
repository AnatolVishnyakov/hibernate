package entities;

import org.hibernate.annotations.SortComparator;

import javax.persistence.*;
import java.util.List;

@Entity
public class Person {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @ElementCollection
    private List<HomeAddress> address;
    @ElementCollection
//    @OrderBy("marks") // сортировка уже полученных из БД значений
//    @OrderColumn
//    @org.hibernate.annotations.OrderBy(clause = "marks DESC")
//    @SortComparator(MarkComparator.class)
    private List<Integer> marks;

    public Person(String name, List<HomeAddress> address) {
        this.name = name;
        this.address = address;
    }

    public Person() {
    }

    public void setMarks(List<Integer> marks) {
        this.marks = marks;
    }

    public List<Integer> getMarks() {
        return marks;
    }
}
