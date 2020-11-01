package com.orm.hibernate.ex.model.entity.mapping.basic;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.util.List;

//@Entity
public class ItemColumnTransformer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column(name = "IMPERIALWEIGHT", precision = 2)
    @ColumnTransformer( // Индекс работать не будет
            read = "IMPERIALWEIGHT / 2.00",
            write = "? * 2.00"
    )
    private float metricWeight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getMetricWeight() {
        return metricWeight;
    }

    public void setMetricWeight(float metricWeight) {
        this.metricWeight = metricWeight;
    }

    @Override
    public String toString() {
        return "ItemColumnTransformer{" +
                "id=" + id +
                ", metricWeight=" + metricWeight +
                '}';
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
//            ItemColumnTransformer item = new ItemColumnTransformer();
//            entityManager.persist(item);
//            item.setMetricWeight(4.0f);

            final List<ItemColumnTransformer> result = entityManager
                    .createQuery("select i from ItemColumnTransformer i where i.metricWeight > :w", ItemColumnTransformer.class)
                    .setParameter("w", 2.0)
                    .getResultList();
            result.forEach(System.out::println);
            result.forEach(item -> System.out.println(item.getMetricWeight()));
        });
    }
}
