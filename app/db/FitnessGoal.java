import java.sql.*;

public class FitnessGoal {
    
    // Insert a fitness goal into the fitness goals table.
    public static boolean addGoal(
        Connection conn,
        Integer memberId,
        Float targetValue,
        String unit,
        String goalType,
        Date targetDate,
        Date startDate,
        boolean active
    ) {
        try {
            String query = "INSERT INTO fitness_goals (member_id, target_value, unit, " 
                + "goal_type, target_date, start_date, active) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            pstmt.setFloat(2, targetValue);
            pstmt.setString(3, unit);
            pstmt.setString(4, goalType);
            pstmt.setDate(5, targetDate);
            pstmt.setDate(6, startDate);
            pstmt.setBoolean(7, active);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    // Update a goal's activity.
    public static boolean updateActivity(Connection conn, Integer goalId, boolean active) {
        try {
            String query = "UPDATE fitness_goals SET active=? WHERE goal_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setBoolean(1, active);
            pstmt.setInt(2, goalId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }
}
