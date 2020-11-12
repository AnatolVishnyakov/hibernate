package com.orm.hibernate.ex.model.complexschemas.compositekey.embedded;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserId implements Serializable { // тип Id должен быть сериализуемым
    protected String username; // автоматически генерируется ограничение NOT NULL
    protected String departmentNr;

    protected UserId() {
    }

    public UserId(String username, String departmentNr) {
        this.username = username;
        this.departmentNr = departmentNr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return username.equals(userId.username) &&
                departmentNr.equals(userId.departmentNr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, departmentNr);
    }
}
