import java.sql.*;

/**
 * Helper class to handle the goal types table in the database.
 */
public class GoalType {
    
    /**
     * Get the name of a goal type by ID.
     * @param conn The connection to the database.
     * @param typeId The ID of the goal type.
     * @return The name of the goal type.
     */
    public static String getName(Connection conn, Integer typeId) {
        try {
            if (!exists(conn, typeId)) return null;
            String query = "SELECT name FROM goal_types WHERE type_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, typeId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            String name = rs.getString("name");
            pstmt.close();
            rs.close();
            return name;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the unit of a goal type by ID.
     * @param conn The connection to the database.
     * @param typeId The ID of the goal type.
     * @return The unit of the goal type.
     */
    public static String getUnit(Connection conn, Integer typeId) {
        try {
            if (!exists(conn, typeId)) return null;
            String query = "SELECT unit FROM goal_types WHERE type_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, typeId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            String unit = rs.getString("unit");
            pstmt.close();
            rs.close();
            return unit;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if a goal type exists with the given ID.
     * @param conn The connection to the database.
     * @param typeId The ID of the goal type.
     * @return True if the goal type exists, false otherwise.
     */
    public static boolean exists(Connection conn, Integer typeId) {
        try {
            String query = "SELECT type_id FROM goal_types WHERE type_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, typeId);
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();
            pstmt.close();
            rs.close();
            return exists;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }
}
