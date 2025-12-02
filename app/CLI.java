
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * CLI for the Fitness Club Management System. Handles menus, user input, and
 * calls helper functions.
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
            Terminal.app("Choice:");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    newMemberMenu();
                    break;
                case "2":
                    existingMemberMenu();
                    break;
                case "3":
                    trainerMenu();
                    break;
                case "4":
                    adminMenu();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    Terminal.error("Invalid choice. Try again.");
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
            Terminal.app("Choice:");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    registerMemberFlow();
                    back = true; // go back to main after registering
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    Terminal.error("Invalid choice. Try again.");
            }
        }
    }

    // EXISTING MEMBER MENU (display only)
    // =====================
    private void existingMemberMenu() {
        boolean back = false;

        while (!back) {
            Terminal.app("\n--- Existing Member ---");
            Terminal.app("Enter your email (0 to go back):");
            String email = sc.nextLine().trim();

            if ("0".equals(email)) {
                back = true;
                continue;
            }

            Integer memberId = controller.getMemberIdByEmail(email);
            if (memberId == null) {
                Terminal.error("No member found with that email. Try again.");
                continue;
            }

            String name = controller.getMemberName(memberId);
            if (name != null) {
                Terminal.app("Welcome, " + name + ".");
            }
            existingMemberActions(memberId);
            back = true;
        }
    }

    // MEMBER FLOWS
    // =====================
    private void registerMemberFlow() {
        try {
            Terminal.app("Full name:");
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

            Terminal.app("Gender:");
            String gender = sc.nextLine().trim();

            Terminal.app("Email:");
            String email = sc.nextLine().trim();

            Terminal.app("Phone:");
            String phone = sc.nextLine().trim();

            boolean ok = controller.registerMember(name, dob, gender, email, phone);
            if (ok) {
                Terminal.app("Member added successfully."); 
            }else {
                Terminal.error("Failed to add member.");
            }

        } catch (Exception e) {
            Terminal.exception(e);
        }
    }

    private void existingMemberActions(Integer memberId) {
        boolean back = false;

        while (!back) {
            Terminal.app("\n--- Existing Member Menu ---");
            Terminal.app("1. Profile management");
            Terminal.app("2. Health history");
            Terminal.app("3. Fitness goals");
            Terminal.app("4. PT session scheduling");
            Terminal.app("5. Class registration");
            Terminal.app("0. Back");
            Terminal.app("(Options 4-5 not available yet.)");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "0":
                    back = true;
                    break;
                case "1":
                    profileManagement(memberId);
                    break;
                case "2":
                    healthHistory(memberId);
                    break;
                case "3":
                    fitnessGoals(memberId);
                    break;
                case "4":
                case "5":
                    Terminal.app("Feature not implemented yet.");
                    break;
                default:
                    Terminal.error("Invalid choice. Try again.");
            }
        }
    }

    private void profileManagement(Integer memberId) {
        boolean back = false;

        while (!back) {
            String currentName = controller.getMemberName(memberId);
            Date currentDob = controller.getMemberDOB(memberId);
            String currentGender = controller.getMemberGender(memberId);
            String currentEmail = controller.getMemberEmail(memberId);
            String currentPhone = controller.getMemberPhone(memberId);

            Terminal.app("\n--- Profile Management ---");
            Terminal.app("1. Update name (current: " + currentName + ")");
            Terminal.app("2. Update date of birth (current: " + currentDob + ")");
            Terminal.app("3. Update gender (current: " + currentGender + ")");
            Terminal.app("4. Update email (current: " + currentEmail + ")");
            Terminal.app("5. Update phone (current: " + currentPhone + ")");
            Terminal.app("0. Back");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "0":
                    back = true;
                    break;
                case "1":
                    updateName(memberId);
                    break;
                case "2":
                    updateDob(memberId);
                    break;
                case "3":
                    updateGender(memberId);
                    break;
                case "4":
                    updateEmail(memberId);
                    break;
                case "5":
                    updatePhone(memberId);
                    break;
                default:
                    Terminal.error("Invalid choice. Try again.");
            }
        }
    }

    private void updateName(Integer memberId) {
        Terminal.app("New full name:");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            Terminal.error("Name cannot be empty.");
            return;
        }
        boolean ok = controller.updateMemberName(memberId, name);
        if (ok) {
            Terminal.app("Name updated."); 
        }else {
            Terminal.error("Failed to update name.");
        }
    }

    private void updateDob(Integer memberId) {
        Terminal.app("Date of birth (YYYY-MM-DD):");
        String dobStr = sc.nextLine().trim();
        try {
            Date dob = Date.valueOf(LocalDate.parse(dobStr));
            boolean ok = controller.updateMemberDOB(memberId, dob);
            if (ok) {
                Terminal.app("Date of birth updated."); 
            }else {
                Terminal.error("Failed to update date of birth.");
            }
        } catch (Exception e) {
            Terminal.error("Invalid date. Please use YYYY-MM-DD.");
        }
    }

    private void updateGender(Integer memberId) {
        Terminal.app("Gender:");
        String gender = sc.nextLine().trim();
        if (gender.isEmpty()) {
            Terminal.error("Gender cannot be empty.");
            return;
        }
        boolean ok = controller.updateMemberGender(memberId, gender);
        if (ok) {
            Terminal.app("Gender updated."); 
        }else {
            Terminal.error("Failed to update gender.");
        }
    }

    private void updateEmail(Integer memberId) {
        Terminal.app("New email:");
        String email = sc.nextLine().trim();
        if (email.isEmpty()) {
            Terminal.error("Email cannot be empty.");
            return;
        }

        boolean ok = controller.updateMemberEmail(memberId, email);
        if (ok) {
            Terminal.app("Email updated."); 
        }else {
            Terminal.error("Failed to update email.");
        }
    }

    private void updatePhone(Integer memberId) {
        Terminal.app("New phone:");
        String phone = sc.nextLine().trim();
        if (phone.isEmpty()) {
            Terminal.error("Phone cannot be empty.");
            return;
        }
        boolean ok = controller.updateMemberPhone(memberId, phone);
        if (ok) {
            Terminal.app("Phone updated."); 
        }else {
            Terminal.error("Failed to update phone.");
        }
    }

    private void healthHistory(Integer memberId) {
        boolean back = false;

        while (!back) {
            Terminal.app("\n--- Health History ---");
            Terminal.app("1. Record new metric");
            Terminal.app("2. View metrics");
            Terminal.app("0. Back");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "0":
                    back = true;
                    break;
                case "1":
                    recordHealthMetric(memberId);
                    break;
                case "2":
                    showHealthMetrics(memberId);
                    break;
                default:
                    Terminal.error("Invalid choice. Try again.");
            }
        }
    }

    private void recordHealthMetric(Integer memberId) {
        try {
            Terminal.app("Heart rate (bpm):");
            Integer heartRate = Integer.parseInt(sc.nextLine().trim());
            Terminal.app("Body fat (%):");
            Float bodyFat = Float.parseFloat(sc.nextLine().trim());
            Terminal.app("Weight (lbs):");
            Integer weight = Integer.parseInt(sc.nextLine().trim());
            Terminal.app("Height (cm):");
            Integer height = Integer.parseInt(sc.nextLine().trim());

            boolean ok = controller.addHealthMetric(memberId, heartRate, bodyFat, weight, height);
            if (ok) {
                Terminal.app("Health metric recorded."); 
            }else {
                Terminal.error("Failed to record health metric.");
            }
        } catch (NumberFormatException e) {
            Terminal.error("Invalid number entered. Please try again.");
        }
    }

    private void showHealthMetrics(Integer memberId) {
        LinkedList<Integer> metrics = controller.getHealthMetrics(memberId);
        if (metrics == null || metrics.isEmpty()) {
            Terminal.app("No metrics recorded yet.");
            return;
        }
        Terminal.app("Metrics (latest first):");
        for (Integer id : metrics) {
            Terminal.app(
                    controller.getHealthMetricTimestamp(id)
                    + " | HR: " + controller.getHealthMetricHeartRate(id)
                    + " bpm | Body fat: " + controller.getHealthMetricBodyFat(id)
                    + " % | Weight: " + controller.getHealthMetricWeight(id)
                    + " lbs | Height: " + controller.getHealthMetricHeight(id) + " cm"
            );
        }
    }

    private void fitnessGoals(Integer memberId) {
        boolean back = false;

        while (!back) {
            Terminal.app("\n--- Fitness Goals ---");
            Terminal.app("1. Create new goal");
            Terminal.app("2. View goals");
            Terminal.app("0. Back");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "0":
                    back = true;
                    break;
                case "1":
                    createFitnessGoal(memberId);
                    break;
                case "2":
                    showFitnessGoals(memberId);
                    break;
                default:
                    Terminal.error("Invalid choice. Try again.");
            }
        }
    }

    private void createFitnessGoal(Integer memberId) {
        try {
            Terminal.app("Goal type ID:");
            Integer typeId = Integer.parseInt(sc.nextLine().trim());
            Terminal.app("Target value:");
            Float targetValue = Float.parseFloat(sc.nextLine().trim());
            Terminal.app("Target date (YYYY-MM-DD):");
            Date targetDate = Date.valueOf(LocalDate.parse(sc.nextLine().trim()));
            Terminal.app("Start date (YYYY-MM-DD, blank for today):");
            String start = sc.nextLine().trim();
            Date startDate = start.isEmpty()
                    ? Date.valueOf(LocalDate.now())
                    : Date.valueOf(LocalDate.parse(start));

            boolean ok = controller.addFitnessGoal(memberId, typeId, targetValue, targetDate, startDate);
            if (ok) {
                Terminal.app("Goal created."); 
            }else {
                Terminal.error("Failed to create goal.");
            }
        } catch (NumberFormatException e) {
            Terminal.error("Invalid number entered. Please try again.");
        } catch (Exception e) {
            Terminal.error("Invalid date. Please use YYYY-MM-DD.");
        }
    }

    private void showFitnessGoals(Integer memberId) {
        LinkedList<Integer> goals = controller.getFitnessGoals(memberId);
        if (goals == null || goals.isEmpty()) {
            Terminal.app("No goals found.");
            return;
        }
        Terminal.app("Goals:");
        for (Integer goalId : goals) {
            Terminal.app(
                    "#" + goalId
                    + " | " + controller.getGoalTypeName(goalId)
                    + " target: " + controller.getGoalTargetValue(goalId)
                    + " " + controller.getGoalTypeUnit(goalId)
                    + " by " + controller.getGoalTargetDate(goalId)
                    + " (start: " + controller.getGoalStartDate(goalId) + ")"
                    + (controller.isGoalCompleted(goalId) ? " [completed]" : "")
            );
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
