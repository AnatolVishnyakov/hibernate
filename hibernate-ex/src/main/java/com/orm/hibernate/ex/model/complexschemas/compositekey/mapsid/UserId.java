package com.orm.hibernate.ex.model.complexschemas.compositekey.mapsid;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserId implements Serializable {
    private String username;
    private Long departmentId;

    public UserId() {
    }

    public UserId(String username, Long departmentId) {
        this.username = username;
        this.departmentId = departmentId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getUsername() {
        return username;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        if (!departmentId.equals(userId.departmentId)) return false;
        if (!username.equals(userId.username)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + departmentId.hashCode();
        return result;
    }
}
