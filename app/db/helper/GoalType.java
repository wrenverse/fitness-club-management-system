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
            String query = "SELECT type_id FROM goal_types WHERE type_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, typeId);
            String name = pstmt.executeQuery(query).getString("name");
            pstmt.close();
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
            String unit = pstmt.executeQuery(query).getString("unit");
            pstmt.close();
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
            boolean exists = pstmt.executeQuery(query).next();
            pstmt.close();
            return exists;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }
}
