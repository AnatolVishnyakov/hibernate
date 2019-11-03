package entities;

import enums.Days;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue
    private int id;
    @Basic(optional = false, fetch = FetchType.LAZY)
    @Type(type = "java.lang.String")
    private String firstName;
    @Type(type = "java.lang.String")
    private String lastName;
    @Type(type = "java.lang.String")
    private String secondName;
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    @Enumerated(value = EnumType.STRING)
    private Days loveDaysWeek;
    @CreationTimestamp
    private Date createTime;
    @UpdateTimestamp
    private Date updateTime;
    @Formula("concat(lastName, ' ', firstName, ' ', secondName)")
    private String fullName;
    private Address address;

    public Student() {
    }

    public Student(String firstName) {
        this.firstName = firstName;
    }

    public Student(String firstName, Days loveDaysWeek) {
        this(firstName);
        this.loveDaysWeek = loveDaysWeek;
    }

    public Student(String firstName, Date birthDate, Days loveDaysWeek) {
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.loveDaysWeek = loveDaysWeek;
    }

    public Student(String firstName, String lastName, String secondName, Days loveDaysWeek) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.secondName = secondName;
        this.loveDaysWeek = loveDaysWeek;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Student{" +
                "fullName='" + fullName + '\'' +
                '}';
    }
}
