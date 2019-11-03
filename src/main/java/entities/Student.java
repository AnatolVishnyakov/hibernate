package entities;

import enums.Days;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Student {
    @Id
    @GeneratedValue
    private int id;
    @Basic(optional = false, fetch = FetchType.LAZY)
    @Type(type = "java.lang.String")
    private String name;
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    @Enumerated(value = EnumType.STRING)
    private Days loveDaysWeek;
    @CreationTimestamp
    private Date createTime;
    @UpdateTimestamp
    private Date updateTime;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public Student(String name, Days loveDaysWeek) {
        this(name);
        this.loveDaysWeek = loveDaysWeek;
    }

    public Student(String name, Date birthDate, Days loveDaysWeek) {
        this.name = name;
        this.birthDate = birthDate;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
