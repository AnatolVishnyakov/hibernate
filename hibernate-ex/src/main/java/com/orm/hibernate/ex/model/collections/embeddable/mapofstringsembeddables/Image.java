package com.orm.hibernate.ex.model.collections.embeddable.mapofstringsembeddables;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Image {
    @Column
    private String title;
    private int width;
    private int height;

    public Image() {
    }

    public Image(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return width == image.width &&
                height == image.height &&
                Objects.equals(title, image.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, width, height);
    }

    @Override
    public String toString() {
        return "Image{" +
                "title='" + title + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
