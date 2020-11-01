package com.orm.hibernate.ex.model.collections.embeddable.setofembeddables;

import org.hibernate.annotations.Parent;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Image {
    @Parent
    private Item item;
    @Column(nullable = false)
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return width == image.width &&
                height == image.height &&
                Objects.equals(item, image.item) &&
                title.equals(image.title) &&
                fileName.equals(image.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, title, fileName, width, height);
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
