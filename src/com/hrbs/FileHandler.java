package com.hrbs;

import com.hrbs.adminService.Admin;
import com.hrbs.roomService.Room;
import com.hrbs.userService.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileHandler {
    public static ArrayList<Room> getAllRooms(String path) {
        ArrayList<Room> rooms = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line = reader.readLine();
            while (line != null) {
                String[] roomDetails = line.split(",");
                int roomNum = Integer.parseInt(roomDetails[0]);
                String type = roomDetails[1];
                String amenities = roomDetails[2];
                double pricePerNight = Double.parseDouble(roomDetails[3]);
                double currentBookingPrice = Double.parseDouble(roomDetails[4]);
                boolean isBooked = roomDetails[5].equals("true");
                rooms.add(new Room(roomNum, type, amenities, pricePerNight, currentBookingPrice, isBooked));
                line = reader.readLine();
            }
        } catch (IOException exception) {
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
        return rooms;
    }

    public static ArrayList<Room> getBookedRooms(String path) {
        ArrayList<Room> bookedRooms = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line = reader.readLine();
            while (line != null) {
                String[] roomDetails = line.split(",");
                int roomNum = Integer.parseInt(roomDetails[0]);
                String type = roomDetails[1];
                String amenities = roomDetails[2];
                double pricePerNight = Double.parseDouble(roomDetails[3]);
                double currentBookingPrice = Double.parseDouble(roomDetails[4]);
                boolean isBooked = roomDetails[5].equals("true");
                if (isBooked) {
                    bookedRooms.add(new Room(roomNum, type, amenities, pricePerNight, currentBookingPrice, true));
                }
                line = reader.readLine();
            }
        } catch (IOException exception) {
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
        return bookedRooms;
    }

    public static ArrayList<Room> getNonBookedRooms(String path) {
        ArrayList<Room> nonBookedRooms = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line = reader.readLine();
            while (line != null) {
                String[] roomDetails = line.split(",");
                int roomNum = Integer.parseInt(roomDetails[0]);
                String type = roomDetails[1];
                String amenities = roomDetails[2];
                double pricePerNight = Double.parseDouble(roomDetails[3]);
                double currentBookingPrice = Double.parseDouble(roomDetails[4]);
                boolean isBooked = roomDetails[5].equals("true");
                if (!isBooked) {
                    nonBookedRooms.add(new Room(roomNum, type, amenities, pricePerNight, currentBookingPrice, false));
                }
                line = reader.readLine();
            }
        } catch (IOException exception) {
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
        return nonBookedRooms;
    }

    public static Room getCurrentRoom(ArrayList<Room> rooms, String command) {
        Scanner sc = new Scanner(System.in);
        if (rooms.isEmpty()) {
            return null;
        }
        int index = Integer.parseInt(command) - 1;
        while (index < 0 || index >= rooms.size()) {
            System.out.println("Invalid index! Please try again!");
            index = Integer.parseInt(sc.nextLine());
        }
        return rooms.get(index);
    }

    public static void updateRoom(Room room) {
        ArrayList<String> updatedLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Main.roomsDatabasePath.toFile()))) {
            String line = reader.readLine();

            while (line != null) {
                String[] roomDetails = line.split(",");
                int roomNum = Integer.parseInt(roomDetails[0]);
                if (room.getRoomNum() == roomNum) {
                    String updatedLine = room.getRoomNum() + "," + room.getType() + "," + String.join("||", room.getAmenities()) + "," + room.getPricePerNight() + "," + room.getCurrentBookingPrice() + "," + room.checkIfBooked();
                    updatedLines.add(updatedLine);
                } else {
                    updatedLines.add(line);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(Main.roomsDatabasePath.toFile()))) {
            for (String line : updatedLines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public static ArrayList<User> getUsers(String path) {
        ArrayList<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line = reader.readLine();
            while (line != null) {
                String[] userDetails = line.split(",");
                String username = userDetails[0];
                String hashedPassword = userDetails[1];
                ArrayList<String> bookingDetails = new ArrayList<>();
                if (userDetails.length >= 3) {
                    bookingDetails.addAll(Arrays.stream(userDetails[2].split("\\|\\|")).toList());
                }
                ArrayList<Integer> bookings = new ArrayList<>();
                if (!bookingDetails.isEmpty()) {
                    for (String bookingDetail : bookingDetails) {
                        bookings.add(Integer.parseInt(bookingDetail));
                    }
                }
                users.add(new User(username, hashedPassword, bookings));
                line = reader.readLine();
            }
        } catch (IOException exception) {
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
        return users;
    }

    public static ArrayList<Admin> getAdmins(String path) {
        ArrayList<Admin> admins = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line = reader.readLine();
            while (line != null) {
                String[] adminDetails = line.split(",");
                String username = adminDetails[0];
                String hashedPassword = adminDetails[1];
                String PIN = adminDetails[2];
                admins.add(new Admin(username, hashedPassword, PIN));
                line = reader.readLine();
            }
        } catch (IOException exception) {
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
        return admins;
    }

    public static void updateUser(User user) {
        ArrayList<String> updatedLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Main.usersDatabasePath.toFile()))) {
            String line = reader.readLine();
            while (line != null) {
                String[] userDetails = line.split(",");
                String username = userDetails[0];

                if (user.getUsername().equals(username)) {
                    String updatedLine = username + "," + user.getPassword() + "," +
                            user.getBookings().stream().map(String::valueOf).collect(Collectors.joining("||"));
                    updatedLines.add(updatedLine);
                } else {
                    updatedLines.add(line);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(Main.usersDatabasePath.toFile()))) {
            for (String line : updatedLines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}