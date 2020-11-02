package com.orm.hibernate.ex.model.collections.embeddable.mapofembeddables;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Filename {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String extension;

    public Filename() {
    }

    public Filename(String name, String extension) {
        this.name = name;
        this.extension = extension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filename filename = (Filename) o;
        return name.equals(filename.name) &&
                extension.equals(filename.extension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, extension);
    }

    @Override
    public String toString() {
        return "Filename{" +
                "name='" + name + '\'' +
                ", extension='" + extension + '\'' +
                '}';
    }
}
