package com.orm.hibernate.ex.model.associations.onetoone.sharedprimarykey;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;

//@Entity
@Table(name = "USERS")
public class User {
    @Id
    private Long id;
    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false // обязательно при ленивой загрузке
    )
    // Рекомендуется использовать, если
    // одна из двух сущностей сохраняется
    // раньше другой
    // (стратегия общего первичного ключа
    // от класса User к классу Address)
    @PrimaryKeyJoinColumn
    private Address shippingAddress;
    private String username;

    public User() {
    }

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            // Зависимый объект необходимо
            // сохранять первым
            Address address = new Address("Green St.", "12345", "New York");
            entityManager.persist(address);

            final User user = new User(address.getId(), "OneUser");
            user.setShippingAddress(address);
            entityManager.persist(user);
        });
    }
}
