import java.sql.*;

/**
 * Helper class to handle the fitness goals table in the database.
 */
public class FitnessGoal {
    
    /**
     * Insert a fitness goal into the fitness goals table.
     * The goal is marked incomplete by default.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @param typeId The fitness goal type.
     * @param targetValue The target value of the goal.
     * @param targetDate The target date to complete the goal by.
     * @param startDate The date the member starts the goal.
     * @return True if successfully added, false otherwise.
     */
    public static boolean add(
        Connection conn,
        Integer memberId,
        Integer typeId,
        Float targetValue,
        Date targetDate,
        Date startDate
    ) {
        try {
            String query = """
                "INSERT INTO fitness_goals (
                    member_id,
                    type_id,
                    target_value,
                    target_date,
                    start_date,
                    is_completed
                ) VALUES (?, ?, ?, ?, ?, ?)
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, typeId);
            pstmt.setFloat(3, targetValue);
            pstmt.setDate(4, targetDate);
            pstmt.setDate(5, startDate);
            pstmt.setBoolean(6, false);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Marked a fitness goal as completed.
     * @param conn The connection to the database.
     * @param goalId The ID of the goal.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean markedCompleted(Connection conn, Integer goalId) {
        try {
            String query = """
                UPDATE fitness_goals
                    SET is_completed = ?
                    WHERE goal_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setBoolean(1, true);
            pstmt.setInt(2, goalId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Get the incomplete fitness goals of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The table rows containing the incomplete goals.
     */
    public static ResultSet getIncompleteGoals(Connection conn, Integer memberId) {
        try {
            String query = """
                SELECT * FROM fitness_goals
                    WHERE member_id = ?
                        AND is_completed = FALSE
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            return pstmt.executeQuery(query);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }
}
