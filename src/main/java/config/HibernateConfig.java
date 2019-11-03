package config;

import entities.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfig {
    public static void main(String[] args) {
        Configuration cfg = new Configuration()
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL94Dialect")
                .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/hibernate")
                .setProperty("hibernate.connection.username", "postgres")
                .setProperty("hibernate.connection.password", "")
                .configure();

        try (SessionFactory sessionFactory = cfg.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(new Student("Tom"));
            session.getTransaction().commit();
        }
    }
}
