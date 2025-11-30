import java.sql.*;

/**
 * Helper class to handle the goal types table in the database.
 */
public class GoalType {
    
    /**
     * Get the information of a goal type by ID.
     * @param conn The connection to the database.
     * @param typeId The ID of the type.
     * @return The table row containing the information.
     */
    public static ResultSet getInfo(Connection conn, Integer typeId) {
        try {
            String query = "SELECT * FROM goal_types WHERE type_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, typeId);
            return pstmt.executeQuery(query);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }
}
