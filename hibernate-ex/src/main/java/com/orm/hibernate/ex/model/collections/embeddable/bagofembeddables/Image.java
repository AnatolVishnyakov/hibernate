package com.orm.hibernate.ex.model.collections.embeddable.bagofembeddables;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Image {
    @Column
    private String title;
    @Column(nullable = false)
    private String fileName;
    private int width;
    private int height;

    public Image() {
    }

    public Image(String title, String fileName, int width, int height) {
        this.title = title;
        this.fileName = fileName;
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
                Objects.equals(title, image.title) &&
                fileName.equals(image.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, fileName, width, height);
    }

    @Override
    public String toString() {
        return "Image{" +
                "title='" + title + '\'' +
                ", fileName='" + fileName + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
