import java.sql.*;

/**
 * Helper class to handle the rooms table in the database.
 */
public class Room {

    /**
     * Get the location of a room by ID.
     * @param conn The connection to the database.
     * @param roomId The ID of the room.
     * @return The location of the room.
     */
    public static String getLocation(Connection conn, Integer roomId) {
        try {
            if (!exists(conn, roomId)) return null;
            String query = "SELECT location FROM rooms WHERE room_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, roomId);
            return pstmt.executeQuery(query).getString("location");
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if a room exists with the given ID.
     * @param conn The connection to the database.
     * @param roomId The ID of the room.
     * @return True if the room exists, false otherwise.
     */
    public static boolean exists(Connection conn, Integer roomId) {
        try {
            String query = "SELECT room_id FROM rooms WHERE room_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, roomId);
            return pstmt.executeQuery(query).next();
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }
}
