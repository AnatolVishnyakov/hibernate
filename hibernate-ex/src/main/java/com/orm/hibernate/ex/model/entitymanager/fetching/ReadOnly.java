package com.orm.hibernate.ex.model.entitymanager.fetching;

import com.orm.hibernate.ex.model.QueryProcessor;
import com.orm.hibernate.ex.model.entitymanager.model.Item;
import org.hibernate.Session;
import org.hibernate.annotations.QueryHints;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

public class ReadOnly {
    private static <T> T saveEntity(T entity) {
        return QueryProcessor.process(entityManager -> {
            entityManager.persist(entity);
            return entity;
        });
    }

    private static Item createItem() {
        final Item item = new Item("test-item-" + UUID.randomUUID().toString());
        saveEntity(item);
        return item;
    }

    private static void readOnlyAllEntity() {
        final Item item = createItem();
        QueryProcessor.process(entityManager -> {
            // Отключение изменений для всех
            // сущностей в рамках контекста
            entityManager.unwrap(Session.class).setDefaultReadOnly(true);

            final Item it = entityManager.find(Item.class, item.getId());
            it.setName(UUID.randomUUID().toString());

            // Операция UPDATE выоплнена не будет
            entityManager.flush();
        });
    }

    private static void readOnlyConcreteEntity() {
        final Item item = createItem();
        QueryProcessor.process(entityManager -> {
            final Item it = entityManager.find(Item.class, item.getId());
            // Отключение изменений конкретной
            // сущности
            entityManager.unwrap(Session.class).setReadOnly(it, true);

            it.setName(UUID.randomUUID().toString());
            entityManager.flush();
        });
    }

    private static void readOnlyHibernateQuery() {
        QueryProcessor.process(entityManager -> {
            final Query<Item> query = entityManager.unwrap(Session.class)
                    .createQuery("select i from Item i", Item.class);

            query.setReadOnly(false);

            final List<Item> result = query.list();
            for (Item itm : result) {
                itm.setName(UUID.randomUUID().toString());
            }
            entityManager.flush();
        });
    }

    private static void readOnlyJpaQuery() {
        QueryProcessor.process(entityManager -> {
            final TypedQuery<Item> query = entityManager.createQuery("select i from Item i", Item.class);

            query.setHint(QueryHints.READ_ONLY, true);

            final List<Item> result = query.getResultList();
            for (Item itm : result) {
                itm.setName(UUID.randomUUID().toString());
            }
            entityManager.flush();
        });
    }

    public static void main(String[] args) {
        readOnlyJpaQuery();
    }
}
