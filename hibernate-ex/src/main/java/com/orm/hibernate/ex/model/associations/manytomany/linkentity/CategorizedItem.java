package com.orm.hibernate.ex.model.associations.manytomany.linkentity;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

//@Entity
@Table(name = "CATEGORY_ITEM")
@Immutable
public class CategorizedItem {
    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "CATEGORY_ID")
        private Long categoryId;
        @Column(name = "ITEM_ID")
        private Long itemId;

        public Id() {
        }

        public Id(Long categoryId, Long itemId) {
            this.categoryId = categoryId;
            this.itemId = itemId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return Objects.equals(categoryId, id.categoryId) &&
                    Objects.equals(itemId, id.itemId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(categoryId, itemId);
        }
    }

    @EmbeddedId
    private Id id = new Id();
    @Column(updatable = false)
    @NotNull
    private String addedBy;
    @Column(updatable = false)
    @NotNull
    private Date addedOn = new Date();
    @ManyToOne
    @JoinColumn(
            name = "CATEGORY_ID",
            insertable = false,
            updatable = false
    )
    private Category category;
    @ManyToOne
    @JoinColumn(
            name = "ITEM_ID",
            insertable = false,
            updatable = false
    )
    private Item item;

    public CategorizedItem() {
    }

    public CategorizedItem(String addedByUsername, Category category, Item item) {
        this.addedBy = addedByUsername;
        this.category = category;
        this.item = item;

        this.id.categoryId = category.getId();
        this.id.itemId = item.getId();

        category.getCategorizedItems().add(this);
        item.getCategorizedItems().add(this);
    }
}
