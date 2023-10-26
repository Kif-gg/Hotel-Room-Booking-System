package com.hrbs;

import com.hrbs.abstractUser.BaseUser;
import com.hrbs.adminService.Admin;
import com.hrbs.hotelService.Hotel;
import com.hrbs.roomService.Room;
import com.hrbs.userService.User;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static Path usersDatabasePath = Paths.get("D:\\Sirma academy\\Java & Spring\\16.Abstraction & Encapsulation\\Hotel Room Booking System\\src\\com\\hrbs\\database\\users.csv");
    public static Path roomsDatabasePath = Paths.get("D:\\Sirma academy\\Java & Spring\\16.Abstraction & Encapsulation\\Hotel Room Booking System\\src\\com\\hrbs\\database\\rooms.csv");
    public static Path adminsDatabasePath = Paths.get("D:\\Sirma academy\\Java & Spring\\16.Abstraction & Encapsulation\\Hotel Room Booking System\\src\\com\\hrbs\\database\\admins.csv");
    public static Path hotelsDatabasePath = Paths.get("D:\\Sirma academy\\Java & Spring\\16.Abstraction & Encapsulation\\Hotel Room Booking System\\src\\com\\hrbs\\database\\hotels.csv");
    public static boolean isLoggedIn = false;
    public static BaseUser currentUser = null;
    public static String currentMenu = MenusUI.logRegMenu;
    private static final Hotel hotel = new Hotel();
    private static boolean isAdminAccessOn;
    public static String adminCommand = "Activate admin login";

    public static boolean checkIfAdminAccessOn() {
        return isAdminAccessOn;
    }

    public static void setIsAdminAccessOn(boolean isAdminAccessOn) {
        Main.isAdminAccessOn = isAdminAccessOn;
    }

    public static Hotel getHotel() {
        return hotel;
    }

    public static void main(String[] args) {
        System.out.println(currentMenu);
        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();
        while (!command.equals("STOP")) {
            if (!isLoggedIn && !command.equals(adminCommand)) {
                switch (command) {
                    case "1":
                        User.login();
                        break;
                    case "2":
                        User.register();
                        break;
                    default:
                        System.out.println("Invalid command, please try again!");
                        break;
                }
            } else if (command.equals(adminCommand)) {
                currentMenu = MenusUI.adminLoginMenu;
                System.out.println(currentMenu);
                Admin.login();
            } else if (!checkIfAdminAccessOn() && currentMenu.equals(MenusUI.mainMenu)) {
                switch (command) {
                    case "0":
                        BaseUser.logout();
                        currentMenu = MenusUI.logRegMenu;
                        break;
                    case "1":
                        ArrayList<Room> allRooms = FileHandler.getAllRooms(roomsDatabasePath.toString());
                        currentMenu = MenusUI.allRoomsListMenu(allRooms);
                        System.out.println(currentMenu);
                        System.out.println("Enter a number from the list for details, or enter 0 to go back.");
                        command = sc.nextLine();
                        if (command.equals("0")) {
                            currentMenu = MenusUI.mainMenu;
                            break;
                        } else {
                            Room room = FileHandler.getCurrentRoom(allRooms, command);
                            if (room == null) {
                                break;
                            }
                            currentMenu = MenusUI.roomDetails(room);
                            System.out.println(currentMenu);
                            command = sc.nextLine();
                            if (!room.checkIfBooked() && command.equals("1")) {
                                System.out.println("Please enter how many nights you want to book the room for:");
                                String nights = sc.nextLine();
                                Pattern nightsInputPattern = Pattern.compile("^\\d{1,3}$");
                                Matcher validNightsInput = nightsInputPattern.matcher(nights);
                                while (!validNightsInput.find()) {
                                    System.out.println("Invalid input! Please enter a number, containing one to three digits!");
                                    nights = sc.nextLine();
                                    validNightsInput = nightsInputPattern.matcher(nights);
                                }
                                room.book(Integer.parseInt(nights));
                                break;
                            } else if (command.equals("0")) {
                                currentMenu = MenusUI.roomDetails(room);
                                break;
                            }

                        }
                        break;
                        //TODO: Add the rest of the functionality to the other options
                    case "2":
                        ArrayList<Room> nonBookedRooms = FileHandler.getNonBookedRooms(roomsDatabasePath.toString());
                        currentMenu = MenusUI.nonBookedRoomsListMenu(nonBookedRooms);
                        System.out.println(currentMenu);
                        System.out.println("Enter a number from the list for details, or enter 0 to go back.");
                        break;
                    case "3":
                        ArrayList<Room> bookedRooms = FileHandler.getBookedRooms(roomsDatabasePath.toString());
                        currentMenu = MenusUI.bookedRoomsListMenu(bookedRooms);
                        System.out.println(currentMenu);
                        System.out.println("Enter a number from the list for details, or enter 0 to go back.");
                        break;
                    case "4":
                        currentMenu = MenusUI.userDetailsMenu();
                        System.out.println(currentMenu);
                        break;
                    default:
                        System.out.println("Invalid command, please try again!");
                        break;
                }
            } else if (checkIfAdminAccessOn()) {
                //TODO: Add the rest of the admin functions
                currentMenu = MenusUI.adminMainMenu;
                switch (command) {
                    case "0":
                        BaseUser.logout();
                        currentMenu = MenusUI.logRegMenu;
                        setIsAdminAccessOn(false);
                        break;
                    case "1":
                        ArrayList<Room> allRooms = FileHandler.getAllRooms(roomsDatabasePath.toString());
                        currentMenu = MenusUI.allRoomsListMenu(allRooms);
                        System.out.println(currentMenu);
                        System.out.println("Enter a number from the list for details, or enter 0 to go back.");
                        break;
                    case "2":
                        ArrayList<Room> nonBookedRooms = FileHandler.getNonBookedRooms(roomsDatabasePath.toString());
                        currentMenu = MenusUI.nonBookedRoomsListMenu(nonBookedRooms);
                        System.out.println(currentMenu);
                        System.out.println("Enter a number from the list for details, or enter 0 to go back.");
                        break;
                    case "3":
                        ArrayList<Room> bookedRooms = FileHandler.getBookedRooms(roomsDatabasePath.toString());
                        currentMenu = MenusUI.bookedRoomsListMenu(bookedRooms);
                        System.out.println(currentMenu);
                        System.out.println("Enter a number from the list for details, or enter 0 to go back.");
                        break;
                    case "4":
                        System.out.println(hotel.getTotalCancellationFees());
                        break;
                    case "5":
                        System.out.println(hotel.getTotalIncome());
                        break;
                    case "6":
                        Room.createRoom();
                        break;
                    default:
                        System.out.println("Invalid command, please try again!");
                        break;
                }
            }
            System.out.println(currentMenu);
            command = sc.nextLine();
        }
    }
}
