package ru.experiment.helloworld;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class MessageRepository {
    public static void main(String[] args) {
        final StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
        serviceRegistryBuilder.applySetting("hibernate.connection.driver_class", "org.postgresql.Driver");
        serviceRegistryBuilder.applySetting("hibernate.connection.url", "jdbc:postgresql://localhost:5432/hibernatelearn");
        serviceRegistryBuilder.applySetting("hibernate.connection.username", "admin");
        serviceRegistryBuilder.applySetting("hibernate.connection.password", "admin");
        serviceRegistryBuilder.applySetting("hibernate.format_sql", "true");
        serviceRegistryBuilder.applySetting("hibernate.use_sql_comments", "true");
        serviceRegistryBuilder.applySetting("hibernate.current_session_context_class", "thread");

        final StandardServiceRegistry serviceRegistry = serviceRegistryBuilder
//                        .configure("hibernate.cfg.xml")
                .build();

        final MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        SessionFactory sessionFactory = metadataSources
                .buildMetadata()
                .buildSessionFactory();

        final Session session = sessionFactory.getCurrentSession();
        final Transaction tx = session.beginTransaction();
        Message message = new Message();
        message.setText("Hello World");
        session.persist(message);
        tx.commit();
    }
}
