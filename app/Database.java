import java.sql.*;

public class Database {

    private Connection conn;

    public Database() {
        conn = null;
    }

    // Connect to the database.
    public boolean connect(String url, String username, String password) {
        Terminal.database("Attempting to establish database connection...");
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            if (conn != null) {
                Terminal.database("Connected to database successfully.");
                this.conn = conn;
                return true;
            }
            Terminal.database("Failed to connect database.");
        } catch (ClassNotFoundException | SQLException e) {
            // Catch any exceptions thrown by the driver.
            Terminal.error("Exception thrown by the PostgreSQL JDBC driver.");
            Terminal.error("Printing stack trace...");
            System.out.println();
            e.printStackTrace();
            System.out.println();
            Terminal.error("Exiting app.");
            System.exit(0);
        }
        return false;
    }

    // Check if the database connection is connected.
    public boolean connectionOpen() { return !(conn == null); }

    // Close the database connection.
    public void closeConnection() throws SQLException { conn.close(); }
}