package com.hrbs.roomService;

import com.hrbs.FileHandler;
import com.hrbs.Main;
import com.hrbs.userService.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Room {
    private int roomNum;
    private String type;
    private ArrayList<String> amenities;
    private double pricePerNight;
    private double cancellationFee;
    private double currentBookingPrice;
    private boolean isBooked;

    public Room(int roomNum, String type, String amenities, double rawPrice) {
        this(roomNum, type, amenities, rawPrice, 0, false);
    }

    public Room(int roomNum, String type, String amenities, double rawPrice, double currentBookingPrice, boolean isBooked) {
        StringBuilder modifyType = new StringBuilder();
        String firstLetter = String.valueOf(type.charAt(0)).toUpperCase(Locale.ROOT);
        modifyType.append(firstLetter).append(type.substring(1).toLowerCase(Locale.ROOT));
        String finalType = modifyType.toString();
        this.roomNum = roomNum;
        this.type = finalType;
        this.amenities = new ArrayList<>(Arrays.asList(amenities.split("\\|\\|")));
        this.currentBookingPrice = currentBookingPrice;

        //        These calculation are just for fancy reasons, didn't want to use just the raw input

        this.pricePerNight = (double) roomNum / 100 * 5 + (this.getTypeMultiplier(type) + 1) * 2.5 + this.getAmenities().size() * 0.05 + rawPrice;
        this.cancellationFee = (double) roomNum / 100 + (this.getTypeMultiplier(type) + 1) * 0.2 + this.getAmenities().size() * 0.01 + rawPrice * 0.1;
        this.isBooked = isBooked;
    }

    private int getTypeMultiplier(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case "standard":
                return 1;
            case "single":
                return 2;
            case "double":
                return 3;
            case "deluxe":
                return 4;
            case "king-size":
                return 5;
            case "suite":
                return 6;
            case "executive suite":
                return 7;
            case "family suite":
                return 8;
            case "penthouse":
                return 9;
            case "adjoining rooms":
                return 10;
            case "accessible room":
                return 11;
            case "honeymoon suite":
                return 12;
            case "extended stay suite":
                return 13;
            default:
                return 0;
        }
    }

    public String getType() {
        return this.type;
    }

    public double getCurrentBookingPrice() {
        return currentBookingPrice;
    }

    public int getRoomNum() {
        return this.roomNum;
    }

    public ArrayList<String> getAmenities() {
        return this.amenities;
    }

    public double getPricePerNight() {
        return this.pricePerNight;
    }

    public double getCancellationFee() {
        return this.cancellationFee;
    }

    public boolean checkIfBooked() {
        return this.isBooked;
    }

    public double getTotalPrice(int nightsToStay) {
        return nightsToStay * getPricePerNight();
    }

    public void setCurrentBookingPrice(double currentBookingPrice) {
        this.currentBookingPrice = currentBookingPrice;
    }

    public void setRoomNum(int roomNum) {
        if (Main.checkIfAdminAccessOn()) {
            this.roomNum = roomNum;
        } else {
            System.out.println("ACCESS DENIED: NO PERMISSION!");
        }
    }

    public void setType(String type) {
        if (Main.checkIfAdminAccessOn()) {
            this.type = type;
        } else {
            System.out.println("ACCESS DENIED: NO PERMISSION!");
        }
    }

    public void setPricePerNight(double rawPrice) {
        if (Main.checkIfAdminAccessOn()) {
            this.pricePerNight = (double) roomNum / 100 * 5 + (this.getTypeMultiplier(this.getType()) + 1) * 2.5 + this.amenities.size() * 0.05 + rawPrice;
        } else {
            System.out.println("ACCESS DENIED: NO PERMISSION!");
        }
    }

    public void setCancellationFee(double rawPrice) {
        if (Main.checkIfAdminAccessOn()) {
            this.cancellationFee = (double) roomNum / 100 * 5 + (this.getTypeMultiplier(this.getType()) + 1) * 2.5 + this.amenities.size() * 0.05 + rawPrice * 0.1;
        } else {
            System.out.println("ACCESS DENIED: NO PERMISSION!");
        }
    }

    public void setAmenities(String amenities) {
        if (Main.checkIfAdminAccessOn()) {
            this.amenities = new ArrayList<>(Arrays.asList(amenities.split(",")));
        } else {
            System.out.println("ACCESS DENIED: NO PERMISSION!");
        }
    }

    public void setBooked(boolean booked) {
        if (Main.checkIfAdminAccessOn() || Main.currentUser != null) {
            this.isBooked = booked;
        } else {
            System.out.println("ACCESS DENIED: NO PERMISSION!");
        }
    }

    public void book(int nights) {
            if (!checkIfBooked()) {
                setBooked(true);
                Main.getHotel().setTotalIncome(getTotalPrice(nights));
                setCurrentBookingPrice(getTotalPrice(nights));
                Main.currentUser.getBookings().add(this.getRoomNum());
                FileHandler.updateUser((User) Main.currentUser);
                getTotalPrice(nights);
                FileHandler.updateRoom(this);
            } else {
                System.out.println("This room has been already booked!");
            }
    }

    public double cancelBooking() {
        if (checkIfBooked()) {
            setBooked(false);
            Main.getHotel().setTotalCancellationFees(getCancellationFee());
            Main.getHotel().setTotalIncome((0 - getCurrentBookingPrice()) + getCancellationFee());
            FileHandler.updateUser((User) Main.currentUser);
            FileHandler.updateRoom(this);
            return getCancellationFee();
        } else {
            System.out.println("You have not booked this room!");
            return 0;
        }
    }

    public static void createRoom() {
        try (PrintWriter out = new PrintWriter(new FileWriter(Main.roomsDatabasePath.toFile(), true))) {
            if (Main.checkIfAdminAccessOn()) {
                Pattern roomNumPattern = Pattern.compile("\\d{3,4}");
                Pattern typePattern = Pattern.compile("^[A-Za-z ]{3,20}$");
                Pattern amenitiesPattern = Pattern.compile("^\\w+(\\s?\\w+)*(\\|{2}\\w+(\\s?\\w+)*)*$");
                Pattern pricePattern = Pattern.compile("^\\d{1,4}(\\.?\\d{1,2})$");
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter room number:");
                String roomNum = sc.nextLine();
                Matcher validRoomNum = roomNumPattern.matcher(roomNum);
                while (!validRoomNum.find()) {
                    System.out.println("Room number is invalid! It must be from 3 to 4 digits!");
                    System.out.println("Enter room number:");
                    roomNum = sc.nextLine();
                    validRoomNum = roomNumPattern.matcher(roomNum);
                }
                boolean roomExists = false;
                ArrayList<Room> rooms = FileHandler.getAllRooms(Main.roomsDatabasePath.toString());
                while (!roomExists) {
                    for (Room currRoom : rooms) {
                        if (currRoom.getRoomNum() == Integer.parseInt(roomNum)) {
                            roomExists = true;
                            break;
                        }
                    }
                    if (roomExists) {
                        System.out.println("A room of this number already exists!");
                        System.out.println("Enter room number:");
                        roomNum = sc.nextLine();
                        roomExists = false;
                    } else {
                        roomExists = true;
                    }
                }
                System.out.println("Enter room type:");
                String type = sc.nextLine();
                Matcher validType = typePattern.matcher(type);
                while (!validType.find()) {
                    System.out.println("Room type is invalid! It must be from 3 to 20 characters, no specials, space allowed!");
                    System.out.println("Enter type:");
                    type = sc.nextLine();
                    validType = typePattern.matcher(type);
                }
                System.out.println("Enter amenities (separated by just \"||\"):");
                String amenities = sc.nextLine();
                Matcher validAmenities = amenitiesPattern.matcher(amenities);
                while (!validAmenities.find()) {
                    System.out.println("Room amenities are invalid! They must be separated by only \"||\"!");
                    System.out.println("Enter amenities:");
                    amenities = sc.nextLine();
                    validAmenities = amenitiesPattern.matcher(amenities);
                }
                System.out.println("Enter initial price (algorithm calculates the final price automatically):");
                String rawPrice = sc.nextLine();
                Matcher validRawPrice = pricePattern.matcher(rawPrice);
                while (!validRawPrice.find()) {
                    System.out.println("Price is invalid! It must be from 1 to 4 digits, decimals allowed up to 2nd number!");
                    System.out.println("Enter price:");
                    rawPrice = sc.nextLine();
                    validRawPrice = pricePattern.matcher(rawPrice);
                }
                Room room = new Room(Integer.parseInt(roomNum), type, amenities, Double.parseDouble(rawPrice));
                out.write(room.getRoomNum() + "," + room.getType() + "," + amenities + "," + room.getPricePerNight() + "," + room.getCurrentBookingPrice() + "," + room.checkIfBooked());
                out.print("\r\n");
            } else {
                System.out.println("ACCESS DENIED: NO PERMISSION!");
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
