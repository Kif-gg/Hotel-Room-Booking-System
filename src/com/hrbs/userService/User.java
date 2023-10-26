package com.hrbs.userService;

import com.hrbs.FileHandler;
import com.hrbs.Main;
import com.hrbs.MenusUI;
import com.hrbs.PseudoHasher;
import com.hrbs.abstractUser.BaseUser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User extends BaseUser {
    private ArrayList<Integer> bookings;

    public User(String username, String password, ArrayList<Integer> bookings) {
        super(username, password);
        this.bookings = bookings;
    }

    public String getUsername() {
        return super.username;
    }

    public String getPassword() {
        return super.password;
    }

    public ArrayList<Integer> getBookings() {
        return this.bookings;
    }
    public void setBookings(ArrayList<Integer> bookings) {
        this.bookings = bookings;
    }

    public static void register() {
        try (PrintWriter out = new PrintWriter(new FileWriter(Main.usersDatabasePath.toFile(), true))) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter your username:");
            String username = sc.nextLine();

//        Patterns can be more complex, made them simple for smooth testing as it is a beginner system

            Pattern usernamePattern = Pattern.compile("^\\w{3,20}$");
            Matcher validUsername = usernamePattern.matcher(username);
            while (!validUsername.find()) {
                System.out.println("Username is invalid! It must be at least 3 characters long, max 20, no whitespaces allowed!");
                System.out.println("Enter your username:");
                username = sc.nextLine();
                validUsername = usernamePattern.matcher(username);
            }
            boolean usernameExists = false;
            ArrayList<User> users = FileHandler.getUsers(Main.usersDatabasePath.toString());
            while (!usernameExists) {
                for (User currUser : users) {
                    if (currUser.getUsername().equals(username)) {
                        usernameExists = true;
                        break;
                    }
                }
                if (usernameExists) {
                    System.out.println("This username already exists!");
                    System.out.println("Enter your username:");
                    username = sc.nextLine();
                    usernameExists = false;
                } else {
                    usernameExists = true;
                }
            }
            System.out.println("Enter your password:");
            String password = sc.nextLine();
            Pattern passwordPattern = Pattern.compile("^\\S{6,30}$");
            Matcher validPassword = passwordPattern.matcher(password);
            while (!validPassword.find()) {
                System.out.println("The password is invalid! It must contain at least 6 characters, max 30, no whitespaces allowed!");
                System.out.println("Enter your password:");
                password = sc.nextLine();
                validPassword = passwordPattern.matcher(password);
            }
            System.out.println("Repeat password:");
            String repass = sc.nextLine();
            while (!password.equals(repass)) {
                System.out.println("Passwords don't match!\r\nRepeat password:");
                repass = sc.nextLine();
            }
            User user = new User(username, PseudoHasher.hash(password), new ArrayList<>());
            out.print(user.getUsername() + "," + user.getPassword() + ",");
            out.print("\r\n");
            System.out.println("Account created successfully!");
            Main.isLoggedIn = true;
            Main.currentMenu = MenusUI.mainMenu;
            Main.currentUser = user;
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public static void login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Username:");
        String username = sc.nextLine();
        System.out.println("Password:");
        String password = sc.nextLine();
        ArrayList<User> users = FileHandler.getUsers(String.valueOf(Main.usersDatabasePath));
        if (users.isEmpty()) {
            System.out.println("No user with username \"" + username + "\" exists!");
            return;
        }
        boolean foundUserCredentials = false;
        for (User user : users) {
            if (user.getUsername().equals(username) && PseudoHasher.verifyPassword(password, user.getPassword())) {
                foundUserCredentials = true;
                Main.currentUser = user;
                break;
            }
        }
        if (foundUserCredentials) {
            System.out.println("Login successful!");
            Main.isLoggedIn = true;
            Main.currentMenu = MenusUI.mainMenu;
        } else {
            System.out.println("Wrong username or password! Please try again!");
            login();
        }
    }
}

