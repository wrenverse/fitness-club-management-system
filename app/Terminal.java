public class Terminal {

    // Declare ANSI colour codes.
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    // Output a program terminal message.
    public static void app(String msg) {
        System.out.println("[" + ANSI_YELLOW + "PROGRAM" + ANSI_RESET + "]: " + msg);
    }

    // Output an error terminal message.
    public static void error(String msg) {
        System.out.println("[" + ANSI_RED + "ERROR" + ANSI_RESET + "]: " + msg);
    }

    // Output a database terminal message.
    public static void database(String msg) {
        System.out.println("[" + ANSI_PURPLE + "DATABASE" + ANSI_RESET + "]: " + msg);
    }

    // Output an exception message.
    public static void exception(Exception e) {
        System.out.println(
            "[" + ANSI_RED + "ERROR" + ANSI_RESET + "]: Encountered an exception."
        );
        System.out.println();
        e.printStackTrace();
        System.out.println();
    }

    // Format a user input.
    public static String formatInput(String input) {
        return ANSI_CYAN + input + ANSI_RESET;
    }

    // Format a database table.
    private static String formatTable(String table) {
        return ANSI_BLUE + table + ANSI_RESET;
    }
}