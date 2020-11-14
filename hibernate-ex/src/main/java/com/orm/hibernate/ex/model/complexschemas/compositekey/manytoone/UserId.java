package com.orm.hibernate.ex.model.complexschemas.compositekey.manytoone;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserId implements Serializable {
    private String username;
    private String departmentNr;

    public UserId() {
    }

    public UserId(String username, String departmentNr) {
        this.username = username;
        this.departmentNr = departmentNr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepartmentNr() {
        return departmentNr;
    }

    public void setDepartmentNr(String departmentNr) {
        this.departmentNr = departmentNr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        if (!departmentNr.equals(userId.departmentNr)) return false;
        if (!username.equals(userId.username)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + departmentNr.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserId{" +
                "username='" + username + '\'' +
                ", departmentNr='" + departmentNr + '\'' +
                '}';
    }
}