package com.orm.hibernate.jta.model.filtering.callback;

public class CurrentUser extends ThreadLocal<User> {
    public static CurrentUser INSTANCE = new CurrentUser();

    private CurrentUser() {
    }
}
