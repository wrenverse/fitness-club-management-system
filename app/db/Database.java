import java.sql.*;

/**
 * Database class to handle interactions with the database.
 */
public class Database {

    private Connection conn;

    public Database() {
        conn = null;
    }

    /**
     * Connect to the database.
     * @param url URL of the database.
     * @param username Username to log in to the database.
     * @param password to log in to the database.
     * @return True if the connection was successful, false otherwise.
     */
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
            
        } catch (ClassNotFoundException e) {
            Terminal.error("Failed to load the PostgreSQL JDBC driver.");
            Terminal.error("Exiting app.");
            System.exit(0);
        } catch (SQLException e) {
            Terminal.error("Incorrect login credentials.");
        }
        return false;
    }

    // Check if the database connection is connected.
    public boolean connectionOpen() { return !(conn == null); }

    // Close the database connection.
    public void closeConnection() throws SQLException { conn.close(); }
}