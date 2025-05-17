/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.midterm;

/**
 *
 * @author Rakk
 */

import java.io.FileReader;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;

public class Midterm {

    /**
     * @param args the command line arguments
     */
    private static final String[] MENU_ITEMS = {"Pizza", "Burger", "Fries"};
    private static final double[] ITEM_PRICES = {8.99, 5.99, 2.99};
    
    public static void main(String[] args) {
       Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to the Restaurant Ordering System!");
        System.out.println("1. Create Account");
        System.out.println("2. Login");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        switch (choice) {
            case 1:
                createAccount(scanner);
                break;
            case 2:
                if (login(scanner)) {
                    orderingMenu(scanner);
                } else {
                    System.out.println("Login failed. Exiting system.");
                }
                break;
            default:
                System.out.println("Invalid option. Exiting system.");
        }
        
        scanner.close();
    }
    
    private static void createAccount(Scanner scanner) {
        System.out.println("\n--- Create Account ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        // Encrypt the password
        String encryptedPassword = caesarCipher(password, 3);
        
        // Save to file
        try (FileWriter writer = new FileWriter("users.txt", true)) {
            writer.write(username + "," + encryptedPassword + "\n");
            System.out.println("Account created successfully!");
        } catch (IOException e) {
            System.out.println("Error saving account: " + e.getMessage());
        }
    }
    
    private static boolean login(Scanner scanner) {
        System.out.println("\n--- Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username)) {
                    // Decrypt stored password and compare
                    String decryptedPassword = caesarCipher(parts[1], -3);
                    if (decryptedPassword.equals(password)) {
                        System.out.println("Login successful!");
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user data: " + e.getMessage());
        }
        
        System.out.println("Invalid username or password.");
        return false;
    }
    
    private static void orderingMenu(Scanner scanner) {
        int[] quantities = new int[MENU_ITEMS.length];
        boolean ordering = true;
        
        System.out.println("\n--- Restaurant Menu ---");
        
        do {
            // Display menu
            System.out.println("\nMenu Items:");
            for (int i = 0; i < MENU_ITEMS.length; i++) {
                System.out.printf("%d. %s - $%.2f\n", i+1, MENU_ITEMS[i], ITEM_PRICES[i]);
            }
            System.out.println("0. Exit/Finish Order");
            
            // Get user choice
            System.out.print("Enter item number (0 to finish): ");
            int choice = scanner.nextInt();
            
            if (choice == 0) {
                ordering = false;
            } else if (choice > 0 && choice <= MENU_ITEMS.length) {
                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                quantities[choice-1] += quantity;
                System.out.printf("Added %d %s(s) to your order.\n", quantity, MENU_ITEMS[choice-1]);
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        } while (ordering);
        
        // Display order summary
        System.out.println("\n--- Order Summary ---");
        double total = 0.0;
        boolean hasItems = false;
        
        for (int i = 0; i < quantities.length; i++) {
            if (quantities[i] > 0) {
                double itemTotal = quantities[i] * ITEM_PRICES[i];
                System.out.printf("%s: %d x $%.2f = $%.2f\n", 
                                  MENU_ITEMS[i], quantities[i], ITEM_PRICES[i], itemTotal);
                total += itemTotal;
                hasItems = true;
            }
        }
        
        if (hasItems) {
            System.out.printf("\nTotal: $%.2f\n", total);
            System.out.println("Thank you for your order!");
        } else {
            System.out.println("No items were ordered.");
        }
    }
    
    private static String caesarCipher(String text, int shift) {
        StringBuilder result = new StringBuilder();
        
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char)(((c - base + shift) % 26 + 26) % 26 + base);
            }
            result.append(c);
        }
        
        return result.toString();
    }
    
}