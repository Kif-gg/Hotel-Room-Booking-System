package com.hrbs;

public class PseudoHasher {
    public static String hash(String password) {
        StringBuilder hashed = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            if (i % 2 == 0) {
                hashed.append((char) (password.charAt(i) + 2));
            } else {
                hashed.append((char) (password.charAt(i) - 2));
            }
        }
        return hashed.toString();
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return hash(password).equals(hashedPassword);
    }
}