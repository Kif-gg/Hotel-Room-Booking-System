package com.hrbs.adminService;

import com.hrbs.FileHandler;
import com.hrbs.Main;
import com.hrbs.MenusUI;
import com.hrbs.PseudoHasher;
import com.hrbs.abstractUser.BaseUser;

import java.util.ArrayList;
import java.util.Scanner;

public class Admin extends BaseUser {
    private String PIN;

    public Admin(String username, String password, String PIN) {
        super(username, password);
        this.PIN = PIN;
    }

    public String getUsername() {
        return super.username;
    }

    public String getPassword() {
        return super.password;
    }

    public String getPIN() {
        return this.PIN;
    }

    public static void login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Username:");
        String username = sc.nextLine();
        System.out.println("Password:");
        String password = sc.nextLine();
        System.out.println("PIN:");
        String PIN = sc.nextLine();
        ArrayList<Admin> admins = FileHandler.getAdmins(String.valueOf(Main.adminsDatabasePath));
        if (admins.isEmpty()) {
            System.out.println("No admin with username \"" + username + "\" exists");
            return;
        }
        boolean foundUserCredentials = false;
        for (Admin admin : admins) {
            if (admin.getUsername().equals(username) && PseudoHasher.verifyPassword(password, admin.getPassword()) && admin.getPIN().equals(PIN)) {
                foundUserCredentials = true;
                Main.currentUser = admin;
                break;
            }
        }
        if (foundUserCredentials) {
            System.out.println("Login successful!");
            Main.isLoggedIn = true;
            Main.setIsAdminAccessOn(true);
            Main.currentMenu = MenusUI.adminMainMenu;
        } else {
            System.out.println("Wrong username, password or PIN! Please try again!");
            login();
        }
    }
    public void setPIN(String PIN) {
        this.PIN = PIN;
    }
}
