package com.orm.hibernate.jta.concurrency.locking;

import com.orm.hibernate.jta.model.Category;
import com.orm.hibernate.jta.model.Item;
import com.orm.hibernate.jta.utils.QueryProcessor;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

public class PessimisticLockExample {
    private static Set<Category> createCategories() {
        return QueryProcessor.process(entityManager -> {
            Set<Category> categories = new HashSet<>();

            final Category category = new Category(UUID.randomUUID().toString());
            entityManager.persist(category);
            categories.add(category);

            for (int i = 0; i < 5; i++) {
                final Item item = new Item(UUID.randomUUID().toString());
                item.setCategory(category);
                item.setBuyNowPrice(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble()));
                entityManager.persist(item);
            }
            return categories;
        });
    }

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final CountDownLatch endLatch = new CountDownLatch(1);
    private static final int WAIT_MILLIS = 500;
    private static final Logger LOGGER = LoggerFactory.getLogger(PessimisticLockExample.class);

    private interface ItemLockRequestCallable {
        void lock(Session session, Item item);
    }

    protected static <T> void executeAsync(Runnable callable, final Runnable completionCallback) {
        final Future future = executorService.submit(callable);
        new Thread(() -> {
            while (!future.isDone()) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
            try {
                completionCallback.run();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }).start();
    }

    protected static void awaitOnLatch(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void testPessimisticLocking(ItemLockRequestCallable primaryLockRequestCallable, ItemLockRequestCallable secondaryLockRequestCallable) {
        QueryProcessor.process(entityManager -> {
            final Session session = entityManager.unwrap(Session.class);
            try {
                Item selectedItem = entityManager.createQuery("select i from Item i order by i.id desc", Item.class)
                        .setMaxResults(1)
                        .getSingleResult();

                Item item = (Item) session
                        .get(Item.class, selectedItem.getId());
                primaryLockRequestCallable
                        .lock(session, item);

                executeAsync(
                        () -> {
                            QueryProcessor.process(_enEntityManager -> {
                                final Session _session = _enEntityManager.unwrap(Session.class);
                                Item _item = (Item) _session
                                        .get(Item.class, selectedItem.getId());
                                secondaryLockRequestCallable
                                        .lock(_session, _item);
                            });
                        },
                        endLatch::countDown
                );

                Thread.sleep(WAIT_MILLIS);
            } catch (StaleObjectStateException | InterruptedException e) {
                LOGGER.info("Optimistic locking failure: ", e);
            }
        });
        awaitOnLatch(endLatch);
    }

    public static void main(String[] args) {
//        final Set<Long> categories = createCategories()
//                .stream().map(Category::getId)
//                .collect(Collectors.toSet());
//
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HibernateEx");
//
//        EntityManager em = emf.createEntityManager();
//        final EntityTransaction tx = em.getTransaction();
//        tx.begin();
//
//        final Long[] ids = categories.toArray(new Long[0]);
//        final List<Item> items = em.createQuery("SELECT i FROM Item i WHERE i.category.id = :categoryId", Item.class)
//                .setLockMode(LockModeType.PESSIMISTIC_READ)
//                .setHint("javax.persistence.lock.timeout", 5_000)
//                .setParameter("categoryId", ids[0])
//                .getResultList();
//
//        em.createQuery("SELECT i FROM Item i WHERE i.category.id = :categoryId", Item.class)
//                .setLockMode(LockModeType.PESSIMISTIC_READ)
//                .setHint("javax.persistence.lock.timeout", 5_000)
//                .setParameter("categoryId", ids[0])
//                .getResultList();
//
//        tx.commit();
//        em.close();
        testPessimisticLocking(
                (session, item) -> {
                    session.buildLockRequest(new LockOptions(LockMode.PESSIMISTIC_READ)).lock(item);
                    LOGGER.info("PESSIMISTIC_READ acquired");
                },
                (session, item) -> {
                    item.setName("USB Flash Memory Stick");
                    session.flush();
                    LOGGER.info("Implicit lock acquired");
                }
        );
    }
}
