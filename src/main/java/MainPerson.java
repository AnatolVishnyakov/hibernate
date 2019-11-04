import entities.HomeAddress;
import entities.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Arrays;

public class MainPerson {
    private static void printMarks(Session session, int id) {
        Person person = session.load(Person.class, id);
        System.out.println(person.getMarks());
    }

    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();

        try (SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            HomeAddress[] addresses = {new HomeAddress("Lenin"), new HomeAddress("Lesina")};
            Person person = new Person("Mike", Arrays.asList(addresses));
            person.setMarks(Arrays.asList(1, 2, 3));
            printMarks(session, 153);
//            session.persist(address);
            session.persist(person);
            session.getTransaction().commit();
        }
    }
}
