import java.sql.*;

/**
 * Helper class to handle the rooms table in the database.
 */
public class Room {
    
    /**
     * Get the information of a room by ID.
     * @param conn The connection to the database.
     * @param roomId The ID of the trainer.
     * @return The table row containing the information.
     */
    public static ResultSet getInfo(Connection conn, Integer roomId) {
        try {
            String query = "SELECT * FROM rooms WHERE room_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, roomId);
            return pstmt.executeQuery(query);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }
}
