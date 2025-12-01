import java.sql.*;
import java.util.LinkedList;

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
            if (targetDate.before(startDate)) return false;
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
     * Update the type of a fitness goal.
     * @param conn The connection to the database.
     * @param goalId The ID of the goal.
     * @param typeId The ID of the type to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateType(Connection conn, Integer goalId, Integer typeId) {
        try {
            if (!(exists(conn, goalId))) return false;
            String query = "UPDATE fitness_goals SET type_id = ? WHERE goal_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, typeId);
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
     * Update the target value of a fitness goal.
     * @param conn The connection to the database.
     * @param goalId The ID of the goal.
     * @param value The value to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateTargetValue(Connection conn, Integer goalId, Float value) {
        try {
            if (!(exists(conn, goalId))) return false;
            String query = "UPDATE fitness_goals SET target_value = ? WHERE goal_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setFloat(1, value);
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
     * Update the target date of a fitness goal.
     * @param conn The connection to the database.
     * @param goalId The ID of the goal.
     * @param date The date to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateTargetDate(Connection conn, Integer goalId, Date date) {
        try {
            if (!(exists(conn, goalId))) return false;
            if (date.before(getStartDate(conn, goalId))) return false;
            String query = "UPDATE fitness_goals SET target_date = ? WHERE goal_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, date);
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
     * Update the start date of a fitness goal.
     * @param conn The connection to the database.
     * @param goalId The ID of the goal.
     * @param date The date to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateStartDate(Connection conn, Integer goalId, Date date) {
        try {
            if (!(exists(conn, goalId))) return false;
            if (getTargetDate(conn, goalId).before(date)) return false;
            String query = "UPDATE fitness_goals SET start_date = ? WHERE goal_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, date);
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
     * Marked a fitness goal as completed.
     * @param conn The connection to the database.
     * @param goalId The ID of the goal.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean markedCompleted(Connection conn, Integer goalId) {
        try {
            if (!(exists(conn, goalId))) return false;
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
     * Get the type ID of a fitness goal by ID.
     * @param conn The connection to the database.
     * @param goalId The ID of the goal.
     * @return The type ID of the fitness goal.
     */
    public static Integer getTypeId(Connection conn, Integer goalId) {
        try {
            if (!exists(conn, goalId)) return null;
            String query = "SELECT type_id FROM fitness_goals WHERE goal_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, goalId);
            Integer id = pstmt.executeQuery(query).getInt("type_id");
            pstmt.close();
            return id;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the target value of a fitness goal by ID.
     * @param conn The connection to the database.
     * @param goalId The ID of the goal.
     * @return The target valueof the fitness goal.
     */
    public static Float getTargetValue(Connection conn, Integer goalId) {
        try {
            if (!exists(conn, goalId)) return null;
            String query = "SELECT target_value FROM fitness_goals WHERE goal_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, goalId);
            Float amount = pstmt.executeQuery(query).getFloat("target_value");
            pstmt.close();
            return amount;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the target date of a goal by ID.
     * @param conn The connection to the database.
     * @param goalId The ID of the goal.
     * @return The target date of the goal.
     */
    public static Date getTargetDate(Connection conn, Integer goalId) {
        try {
            if (!exists(conn, goalId)) return null;
            String query = "SELECT target_date FROM fitness_goals WHERE goal_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, goalId);
            Date date = pstmt.executeQuery(query).getDate("target_date");
            pstmt.close();
            return date;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the start date of a goal by ID.
     * @param conn The connection to the database.
     * @param goalId The ID of the goal.
     * @return The start date of the goal.
     */
    public static Date getStartDate(Connection conn, Integer goalId) {
        try {
            if (!exists(conn, goalId)) return null;
            String query = "SELECT start_date FROM fitness_goals WHERE goal_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, goalId);
            Date date = pstmt.executeQuery(query).getDate("start_date");
            pstmt.close();
            return date;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if a fitness goal with the given ID is completed.
     * @param conn The connection to the database.
     * @param goalId The ID of the goal.
     * @return True if the fitness goal is completed, false otherwise.
     */
    public static boolean isCompleted(Connection conn, Integer goalId) {
        try {
            if (!exists(conn, goalId)) return false;
            String query = "SELECT is_completed FROM fitness_goals WHERE goal_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, goalId);
            boolean isCompleted = pstmt.executeQuery(query).getBoolean("is_completed");
            pstmt.close();
            return isCompleted;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }

    /**
     * Get the IDs of incomplete goals of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The IDs of the incomplete goals.
     */
    public static LinkedList<Integer> getIncompleteGoals(Connection conn, Integer memberId) {
        try {
            String query = """
                SELECT goal_id FROM fitness_goals
                    WHERE member_id = ?
                        AND is_completed = FALSE
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery(query);
            LinkedList<Integer> ids = new LinkedList<>();
            while (rs.next()) ids.add(rs.getInt("goal_id"));
            pstmt.close();
            rs.close();
            return ids;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if a fitness goal exists with the given ID.
     * @param conn The connection to the database.
     * @param goalId The ID of the goal.
     * @return True if the fitness goal exists, false otherwise.
     */
    public static boolean exists(Connection conn, Integer goalId) {
        try {
            String query = "SELECT goal_id FROM fitness_goals WHERE goal_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, goalId);
            boolean exists = pstmt.executeQuery(query).next();
            pstmt.close();
            return exists;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }
}
