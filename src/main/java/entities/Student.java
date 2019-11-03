package entities;

import enums.Days;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Student {
    @Id
    @GeneratedValue
    private int id;
    @Basic(optional = false, fetch = FetchType.LAZY)
    @Column(unique = true, nullable = false)
    @Type(type = "java.lang.String")
    private String name;
    @Type(type = "java.util.Date")
    private Date birthDate;
    @Enumerated(value = EnumType.STRING)
    private Days loveDaysWeek;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public Student(String name, Days loveDaysWeek) {
        this(name);
        this.loveDaysWeek = loveDaysWeek;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Days getLoveDaysWeek() {
        return loveDaysWeek;
    }

    public void setLoveDaysWeek(Days loveDaysWeek) {
        this.loveDaysWeek = loveDaysWeek;
    }
}
