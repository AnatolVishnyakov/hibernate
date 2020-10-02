package com.orm.model;

import java.io.Serializable;

public class User implements Serializable {
    protected String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
