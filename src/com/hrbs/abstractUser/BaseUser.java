package com.hrbs.abstractUser;

import com.hrbs.Main;

import java.util.ArrayList;

public abstract class BaseUser {
    protected String username;
    protected String password;

    public BaseUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ArrayList<Integer> getBookings() {
        return null;
    }

    public abstract String getUsername();

    public abstract String getPassword();

    public static void logout() {
        Main.isLoggedIn = false;
        Main.currentUser = null;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
