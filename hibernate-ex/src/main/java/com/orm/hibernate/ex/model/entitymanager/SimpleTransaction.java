package com.orm.hibernate.ex.model.entitymanager;

import com.orm.hibernate.ex.model.QueryProcessor;
import com.orm.hibernate.ex.model.entitymanager.model.Address;
import com.orm.hibernate.ex.model.entitymanager.model.Item;
import com.orm.hibernate.ex.model.entitymanager.model.User;
import org.hibernate.Hibernate;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;

public class SimpleTransaction {
    // Сущность находится во временном состоянии
    private static <T> boolean isDraft(EntityManager em, T entity) {
        return em.getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .getIdentifier(entity) == null;
    }

    // Сущность находится в хранимом состоянии
    private static <T> boolean isPersist(EntityManager em, T entity) {
        return em.contains(entity);
    }

    // Сущность находится в отсоединенном состоянии
    private static <T> boolean isDetach(EntityManager em, T entity) {
        return !isPersist(em, entity) &&
                !isDraft(em, entity);
    }

    // Загружен объект из БД в контекст хранения
    private static <T> boolean isLoaded(EntityManager em, T entity) {
        return em.getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .isLoaded(entity);
    }

    private static <T> void printStateEntity(EntityManager em, T entity) {
        final String message = format(
                "\n\t Persist: \t%s \t|" +
                        "\n\t Detach: \t%s \t|" +
                        "\n\t Draft: \t%s \t|" +
                        "\n\t Loaded: \t%s \t|" +
                        "\n",
                isPersist(em, entity),
                isDetach(em, entity),
                isDraft(em, entity),
                isLoaded(em, entity)
        );

        System.out.println(message);
    }

    public static <T> void newEntity(Class<T> clazz) {
        final int number = new Random().nextInt(100_000);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HibernateEx");

        EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            final T entity = clazz.newInstance();
            if (clazz.isInstance(Item.class)) {
                ((Item) entity).setName("item-" + number);
            }

            printStateEntity(em, entity);
            em.persist(entity);
            printStateEntity(em, entity);

            em.detach(entity);
            printStateEntity(em, entity);

            // Синхронизирует/извлекает persistence context
            tx.commit();
        } catch (Exception exc) {
            // Откат транзакции
            tx.rollback();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public static <T> T saveEntity(T entity) {
        return QueryProcessor.process(entityManager -> {
            entityManager.persist(entity);
            return entity;
        });
    }

    public static void exampleEqualsReference() {
        QueryProcessor.process(entityManager -> {
            final Item itemA = entityManager.find(Item.class, 71L);
            final Item itemB = entityManager.find(Item.class, 71L);
            System.out.println(itemA == itemB);
            System.out.println(itemA.equals(itemB));
            System.out.println(itemA.getId().equals(itemB.getId()));
        });
    }

    public static void exampleGetAndUpdateItem() {
        QueryProcessor.process(entityManager -> {
            final Item item = entityManager.find(Item.class, 70L);
            if (item != null) {
                item.setName("New Name " + new Random().nextInt());
                item.setAuctionEnd(new Date());
            }
        });
    }

    // Если не нужно обращаться к БД
    // используется getReference
    public static void exampleGetReferenceItem() {
        QueryProcessor.process(entityManager -> {
            final Item item = entityManager.getReference(Item.class, 72L);
            System.out.println(item.getId());

            printStateEntity(entityManager, item);
            // Загрузка данных прокси-объекта
            Hibernate.initialize(item);
            printStateEntity(entityManager, item);
        });
    }

    private static Item saveItemRandom() {
        Item newItem = new Item("item-" + new Random().nextInt(10_000));
        return saveEntity(newItem);
    }

    private static void exampleDraftState() {
        final Item createdItem = saveItemRandom();

        QueryProcessor.process(entityManager -> {
            final Item item = entityManager.find(Item.class, createdItem.getId());
            System.out.println("Saved Id: " + createdItem.getId());
            printStateEntity(entityManager, item);
            entityManager.remove(item);
            printStateEntity(entityManager, item);

            saveEntity(item);
            System.out.println("Change Id: " + item.getId());
        });
    }

    // Полезно в случае, если хочется отменить
    // сделанные изменения в памяти
    private static void exampleChangeEntityInMemory() {
        final Item createdItem = saveItemRandom();
        System.out.println("Old Name: " + createdItem.getName());

        AtomicBoolean changeItem = new AtomicBoolean(false);
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            System.out.println("Ожидаю команды изменения сущности");
            while (true) {
                if (changeItem.get()) {
                    QueryProcessor.process(entityManager -> {
                        final Item item = entityManager.find(Item.class, createdItem.getId());
                        item.setName("Some Item-" + UUID.randomUUID().toString());
                    });
                    break;
                }
            }
            System.out.println("Сущность изменена");
        });
        executorService.shutdown();

        QueryProcessor.process(entityManager -> {
            changeItem.set(true);
            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final Item item = entityManager.find(Item.class, createdItem.getId());
            entityManager.refresh(item);
            System.out.println("New Name: " + item.getName());
        });
    }

    private static void replicate() {
        // 1
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ReplicateFromPU");
//
//        EntityManager em = emf.createEntityManager();
//        final EntityTransaction tx = em.getTransaction();
//        tx.begin();
//
//        for (int i = 0; i < 100; i++) {
//            em.persist(new Item("test-item-" + i));
//        }
//
//        tx.commit();
//        em.close();
//
        EntityManagerFactory emfA = Persistence.createEntityManagerFactory("ReplicateFromPU");
        final EntityManager emA = emfA.createEntityManager();

        EntityManagerFactory emfB = Persistence.createEntityManagerFactory("ReplicateToPU");
        final EntityManager emB = emfB.createEntityManager();
        final EntityTransaction tx = emB.getTransaction();
        tx.begin();

        for (int i = 1; i <= 100; i++) {
            final Item item = emA.find(Item.class, Integer.toUnsignedLong(i));
            emB.unwrap(Session.class).replicate(item, ReplicationMode.EXCEPTION);
        }

        tx.commit();
        emA.close();
        emB.close();
    }

    private static void exampleFlushEntity() {
        final Item createdItem = saveItemRandom();

        QueryProcessor.process(entityManager -> {
            final Item item = entityManager.find(Item.class, createdItem.getId());
            item.setName("change-" + UUID.randomUUID().toString());
            // Отключает выталкивание контекста перед
            // выполнением запроса, поэтому данные в БД
            // различаются с данными в памяти
            entityManager.setFlushMode(FlushModeType.COMMIT);
            // В любое время можно вызвать flush
            // для синхронизации состояний БД и памяти
//            entityManager.flush();

            final String newName = (String) entityManager.createQuery("select i.name from Item i where i.id = :id")
                    .setParameter("id", createdItem.getId())
                    .getSingleResult();
            System.out.println(item.getName().equals(newName));
        });
    }

    private static void exampleIdentityEntities() {
        final Item createdItem = saveItemRandom();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HibernateEx");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        // один контекст
        tx.begin();

        final Item a = em.find(Item.class, createdItem.getId());
        final Item b = em.find(Item.class, createdItem.getId());
        System.out.println(a == b);
        System.out.println(a.equals(b));
        System.out.println(a.getId().equals(b.getId()));

        tx.commit();
        em.close();

        em = emf.createEntityManager();
        tx = em.getTransaction();
        // другой контекст
        tx.begin();

        final Item c = em.find(Item.class, createdItem.getId());
        System.out.println(a == c);
        System.out.println(a.equals(c));
        System.out.println(a.getId().equals(c.getId()));

        tx.commit();
        em.close();

        final HashSet<Item> allItems = new HashSet<>();
        allItems.add(a);
        allItems.add(b);
        allItems.add(c);

        System.out.println(a.getId() + " : " + a.hashCode());
        System.out.println(b.getId() + " : " + b.hashCode());
        System.out.println(c.getId() + " : " + c.hashCode());
        System.out.println(allItems.size());
    }

    private static void exampleEqualsEntities() {
        final User createdUser = QueryProcessor.process(entityManager -> {
            final User user = new User(UUID.randomUUID().toString());
            Address address = new Address("Green St.", "42342", "New York");
            user.setHomeAddress(address);
            entityManager.persist(user);
            return user;
        });

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HibernateEx");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        final User a = em.find(User.class, createdUser.getId());
        final User b = em.find(User.class, createdUser.getId());
        System.out.println(a == b);
        System.out.println(a.equals(b));
        System.out.println(a.getId().equals(b.getId()));

        tx.commit();
        em.close();

        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();

        final User c = em.find(User.class, createdUser.getId());
        System.out.println(a.getId() + " : " + a.hashCode());
        System.out.println(b.getId() + " : " + b.hashCode());
        System.out.println(c.getId() + " : " + c.hashCode());

        final HashSet<Object> allUsers = new HashSet<>();
        allUsers.add(a);
        allUsers.add(b);
        allUsers.add(c);
        System.out.println(allUsers.size());
    }

    private static void exampleDetachEntity() {
        User savedUser = QueryProcessor.process(entityManager -> {
            final User user = new User(UUID.randomUUID().toString());
            Address address = new Address("Green St.", "42342", "New York");
            user.setHomeAddress(address);
            entityManager.persist(user);
            return user;
        });

        QueryProcessor.process(entityManager -> {
            final User user = entityManager.find(User.class, savedUser.getId());
            entityManager.detach(user);

            System.out.println(entityManager.contains(user));
        });
    }

    private static void exampleMergeEntity() {
        User detachedUser = QueryProcessor.process(entityManager -> {
            final User user = new User(UUID.randomUUID().toString());
            Address address = new Address("Green St.", "42342", "New York");
            user.setHomeAddress(address);
            entityManager.persist(user);
            return user;
        });

        detachedUser.setUsername("test-detached-user");

        QueryProcessor.process(entityManager -> {
            // ссылка detached после слияния
            // не нужна, пользователь заперсистен
            final User mergedUser = entityManager.merge(detachedUser);
            mergedUser.setUsername("test-merged-user-" + UUID.randomUUID().toString());
        });
    }

    public static void main(String[] args) {
        exampleMergeEntity();
    }
}
