package com.orm.model;

import java.io.Serializable;

public class User implements Serializable {
    protected String username;

    public String getUsername() {
        // TODO Проверить обращение по new String(username);
        // TODO Проверить возврат новой коллекции .toArray(new String[])
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
