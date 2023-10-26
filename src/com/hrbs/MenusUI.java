package com.hrbs;

import com.hrbs.roomService.Room;

import java.util.ArrayList;

public class MenusUI {
    public static final String logRegMenu = """
            ╭───────────────────────────────────╮
            │             Welcome!              │
            │  Please enter the number of the   │
            │  option of your choice:           │
            │ 1. Login                          │
            │ 2. Register                       │
            ╰───────────────────────────────────╯""";
    public static final String adminLoginMenu = """
            ╭───────────────────────────────────╮
            │   Admin login access requested!   │
            │ Enter username, password and PIN! │
            ╰───────────────────────────────────╯""";
    public static final String mainMenu = """
            ╭───────────────────────────────────╮
            │             Main menu             │
            │ 1. View all rooms                 │
            │ 2. View non-booked rooms          │
            │ 3. View booked rooms              │
            │ 4. View profile                   │
            │ 0. Logout                         │
            ╰───────────────────────────────────╯""";
    public static final String adminMainMenu = """
            ╭───────────────────────────────────╮
            │             Main menu             │
            │ 1. View all rooms                 │
            │ 2. View non-booked rooms          │
            │ 3. View booked rooms              │
            │ 4. View total cancellation fees   │
            │ 5. View total income              │
            │ 6. Add room                       │
            │ 0. Logout                         │
            ╰───────────────────────────────────╯""";

    public static String allRoomsListMenu(ArrayList<Room> rooms) {
        StringBuilder roomsListed = new StringBuilder();
        for (int i = 1; i <= rooms.size(); i++) {
            Room currRoom = rooms.get(i - 1);
            appendRoomsStringBuilder(roomsListed, i, currRoom.getRoomNum(), currRoom.getType());
        }
        return """
                ╭───────────────────────────────────╮
                │          All rooms list           │
                │ 0. Go back                        │
                │                                   │
                """ +
                roomsListed + """
                ╰───────────────────────────────────╯""";
    }

    public static String nonBookedRoomsListMenu(ArrayList<Room> rooms) {
        StringBuilder roomsListed = new StringBuilder();
        for (int i = 1; i <= rooms.size(); i++) {
            Room currRoom = rooms.get(i - 1);
            appendRoomsStringBuilder(roomsListed, i, currRoom.getRoomNum(), currRoom.getType());
        }
        return """
                ╭───────────────────────────────────╮
                │       Non-booked rooms list       │
                │ 0. Go back                        │
                │                                   │
                """ +
                roomsListed + """
                ╰───────────────────────────────────╯""";
    }

    public static String bookedRoomsListMenu(ArrayList<Room> rooms) {
        StringBuilder roomsListed = new StringBuilder();
        for (int i = 1; i <= rooms.size(); i++) {
            Room currRoom = rooms.get(i - 1);
            appendRoomsStringBuilder(roomsListed, i, currRoom.getRoomNum(), currRoom.getType());
        }
        return """
                ╭───────────────────────────────────╮
                │         Booked rooms list         │
                │ 0. Go back                        │
                │                                   │
                """ +
                roomsListed + """
                ╰───────────────────────────────────╯""";
    }

    public static void appendRoomsStringBuilder(StringBuilder sb, int index, int roomNum, String type) {
        sb.append("│ ")
                .append(index)
                .append(". ")
                .append(roomNum)
                .append(" -> ")
                .append(type)

//                    This calculation is used to keep the UI box from breaking away from the usual design and to keep it nice looking

                .append(" ".repeat(28 - (Integer.toString(index).length() + Integer.toString(roomNum).length() + type.length())))
                .append("│\r\n");
    }

    public static String userDetailsMenu() {
        String username = Main.currentUser.getUsername();
        String boxFixer = "│ ● Username: " + username + " ".repeat(22 - username.length()) + "│" + "\r\n" + "│                                   │\r\n";
        return """
                ╭───────────────────────────────────╮
                │            My profile             │
                """ +
                boxFixer + """
                │ 1. Check my bookings              │
                │ 2. Change password                │
                │ 0. Go back                        │
                ╰───────────────────────────────────╯""";
    }

    public static String roomDetails(Room room) {
        StringBuilder details = new StringBuilder();

//        Appending the values one by one for better readability

        details
                .append("│ ● Number: ")
                .append(room.getRoomNum())
                .append(" ".repeat(24 - Integer.toString(room.getRoomNum()).length()))
                .append("│\r\n");

        details
                .append("│ ● Type: ")
                .append(room.getType())
                .append(" ".repeat(26 - room.getType().length()))
                .append("│\r\n")
                .append("│ ● Amenities:                      │\r\n");

        for (String amenity : room.getAmenities()) {
            details
                    .append("│   ◆ ")
                    .append(amenity)
                    .append(" ".repeat(30 - amenity.length()))
                    .append("│\r\n");
        }

        details
                .append("│ ● Price per night: ")
                .append(room.getPricePerNight())
                .append(" ".repeat(15 - Double.toString(room.getPricePerNight()).length()))
                .append("│\r\n");

        details
                .append("│ ● Available: ")
                .append(room.checkIfBooked() ? "No" : "Yes")
                .append(" ".repeat((room.checkIfBooked() ? 19 : 18)))
                .append("│\r\n")
                .append("│                                   │\r\n");

        if (!room.checkIfBooked()) {
            details.append("│ 1. Book room                      │\r\n");
        }

        return """
                ╭───────────────────────────────────╮
                │              Details              │
                """ +
                details + """
                │ 0. Go back                        │
                ╰───────────────────────────────────╯""";
    }
}
