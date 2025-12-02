import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * CLI for the Fitness Club Management System.
 * Handles menus, user input, and calls helper functions.
 */
public class CLI {

    private final Connection conn;
    private final Controller controller;
    private final Scanner sc;

    public CLI(Connection conn, Controller controller, Scanner sc) {
        this.conn = conn;
        this.controller = controller;
        this.sc = sc;
    }

    // MAIN MENU
    // =====================
    public void run() {
        boolean running = true;

        while (running) {
            Terminal.app("\n=== Fitness Club Management System ===");
            Terminal.app("Select your role:");
            Terminal.app("1. New member");
            Terminal.app("2. Existing member");
            Terminal.app("3. Trainer");
            Terminal.app("4. Administrative staff");
            Terminal.app("0. Exit");
            Terminal.app("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> newMemberMenu();
                case "2" -> existingMemberMenu();
                case "3" -> trainerMenu();
                case "4" -> adminMenu();
                case "0" -> running = false;
                default -> Terminal.error("Invalid choice. Try again.");
            }
        }
    }

    // NEW MEMBER MENU (display only)
    // =====================
    private void newMemberMenu() {
        boolean back = false;

        while (!back) {
            Terminal.app("\n--- New Member Menu ---");
            Terminal.app("1. Register member");
            Terminal.app("0. Back");
            Terminal.app("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    registerMemberFlow();
                    back = true; // go back to main after registering
                }
                case "0" -> back = true;
                default -> Terminal.error("Invalid choice. Try again.");
            }
        }
    }

    // EXISTING MEMBER MENU (display only)
    // =====================
    private void existingMemberMenu() {
        Terminal.app("\n--- Existing Member Menu ---");
        Terminal.app("1. Profile management");
        Terminal.app("2. Health history");
        Terminal.app("3. PT session scheduling");
        Terminal.app("4. Class registration");
        Terminal.app("0. Back");
        Terminal.app("(Functionality not implemented yet.)");
    }

    // MEMBER FLOWS
    // =====================
    private void registerMemberFlow() {
        try {
            Terminal.app("Enter full name:");
            String name = sc.nextLine().trim();

            Date dob = null;
            while (dob == null) {
                Terminal.app("Enter date of birth (YYYY-MM-DD):");
                String dobStr = sc.nextLine().trim();
                try {
                    dob = Date.valueOf(LocalDate.parse(dobStr));
                } catch (Exception e) {
                    Terminal.error("Invalid date. Please use YYYY-MM-DD.");
                }
            }

            Terminal.app("Enter gender:");
            String gender = sc.nextLine().trim();

            Terminal.app("Enter email:");
            String email = sc.nextLine().trim();

            Terminal.app("Enter phone:");
            String phone = sc.nextLine().trim();

            boolean ok = controller.registerMember(name, dob, gender, email, phone);
            if (ok) Terminal.app("Member added successfully.");
            else Terminal.error("Failed to add member.");

        } catch (Exception e) {
            Terminal.exception(e);
        }
    }

    // TRAINER MENU (stub)
    // =====================
    private void trainerMenu() {
        Terminal.app("Trainer menu not implemented yet.");
    }

    // ADMIN MENU (stub)
    // =====================
    private void adminMenu() {
        Terminal.app("Administrative staff menu not implemented yet.");
    }
}
