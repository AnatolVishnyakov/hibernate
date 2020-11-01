package com.orm.hibernate.ex.model.entity.mapping.types;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.Session;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.engine.jdbc.StreamUtils;

import javax.persistence.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;

//@Entity
public class ItemBinary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Lob
    private Blob image;
    @Lob
    private Clob description;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateTime;

    public ItemBinary() {
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public Clob getDescription() {
        return description;
    }

    public void setDescription(Clob description) {
        this.description = description;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
//            final ItemBinary item = new ItemBinary();
//            item.setImage(BlobProxy.generateProxy("test".getBytes()));
//            item.setDescription(ClobProxy.generateProxy("test"));
//            entityManager.persist(item);

            // Выгрузка в память
            final ItemBinary item = entityManager.find(ItemBinary.class, 35L);
            final Blob image = item.getImage();

            try {
                final InputStream imageDataStream = image.getBinaryStream();
                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                StreamUtils.copy(imageDataStream, outputStream);
                final byte[] imageBytes = outputStream.toByteArray();
                System.out.println(new String(imageBytes));
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }

            // Выгрузка напрямую в БД
            Session session = entityManager.unwrap(Session.class);
            try {
                final Blob blob = session.getLobHelper()
                        .createBlob(image.getBinaryStream(), image.length());
                item.setImage(blob);
                entityManager.persist(item);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
