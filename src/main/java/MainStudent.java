import entities.Address;
import entities.Student;
import enums.Days;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class MainStudent {
    private static void save(Session session, Student student) {
        System.out.println("SAVE");
        session.save(student);
    }

    private static Student load(Session session, int id) {
        System.out.println("LOAD");
        return session.load(Student.class, id);
    }

    private static Student get(Session session, int id) {
        System.out.println("GET");
        return session.get(Student.class, id);
    }

    private static int lastStudentId(Session session) {
        System.out.println("GET LAST student ID");
        return (int) session.createSQLQuery("select max(id) from student")
                .getResultList()
                .get(0);
    }

    private static void testAddress(Session session) {
        System.out.println("test Address save");
        Student student = new Student("Ivan", "Ivanov", "Ivanovich", Days.FRIDAY);
        student.setAddress(new Address("Moscow", "Lenina St.", "10"));
        save(session, student);
    }

    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();

        try (SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
//            session.save(new Student("Peter", Days.FRIDAY));
//            System.out.println(load(session, 1));
//            System.out.println(get(session, 1));
            testAddress(session);
//            Student student = get(session, lastStudentId(session));
//            System.out.println(student.getFullName());
//            System.out.println(student.getAddress());
            session.getTransaction().commit();
        }
    }
}
