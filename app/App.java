import java.util.*;

/**
 * App class to initialize the application.
 */
public class App {

    Database db;

    public App() {
        db = new Database();
    }

    /**
     * Connect to the database.
     * @param url URL of the database.
     * @param username Username to log in to the database.
     * @param password to log in to the database.
     * @return True if the connection was successful, false otherwise.
     */
    public boolean connectDatabase(String url, String username, String password) {
        return db.connect(url, username, password);
    }

    // The main function.
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        App app = new App();

        while(true) {
            Terminal.app("Please enter your database's hostname.");
            String host = sc.nextLine();
            Terminal.app("Please enter your database's port number.");
            String port = sc.nextLine();
            Terminal.app("Please enter your database's name.");
            String dbName = sc.nextLine();
            Terminal.app("Please enter your database username.");
            String username = sc.nextLine();
            Terminal.app("Please enter your database password.");
            String password = sc.nextLine();
            
            if (
                app.connectDatabase(
                    "jdbc:postgresql://" + host + ":" + port + "/" + dbName,
                    username,
                    password
                )
            ) break;
            Terminal.app("Attempting login again...");
        }

        Controller controller = new Controller(app.db.getConnection());
        CLI cli = new CLI(app.db.getConnection(), controller, sc);
        cli.run();

        try {
            app.db.closeConnection();
        } catch (Exception e) {
            Terminal.exception(e);
        }
        sc.close();
    }
}
