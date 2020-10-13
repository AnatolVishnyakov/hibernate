package com.orm.hibernate.ex.model.entity.subselect;

import com.orm.hibernate.ex.model.EntitySaver;

public class ItemEx {
    private void generate() {
        EntitySaver.save(entityManager -> {
            final ItemTwo itemTwo = new ItemTwo();
            itemTwo.setName("test");
            entityManager.persist(itemTwo);

            ItemOne itemOne = new ItemOne();
            itemOne.setName("test");
            itemOne.setItemTwo(itemTwo);
            entityManager.persist(itemOne);
        });
    }

    public static void main(String[] args) {
        EntitySaver.save(entityManager -> {
            final ItemSubselect subselect = entityManager.find(ItemSubselect.class, 12L);
            System.out.println(subselect);
        });
    }
}
