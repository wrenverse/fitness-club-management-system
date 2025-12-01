import java.sql.*;

/**
 * Helper class to handle the class registration table in the database.
 */
public class ClassRegistration {
    
    /**
     * Insert a class registration into the class registration table.
     * @param conn The connection to the database.
     * @param class_id The ID of the class.
     * @param member_id The ID of the class.
     * @return True if successfully added, false otherwise.
     */
    public static boolean add(
        Connection conn,
        Integer classId,
        Integer memberId
    ) {
        try {
            // Prevent registration to a full class.
            String query = """
                SELECT COUNT(*)
                    FROM class_registration
                    WHERE member_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            Integer classRegistrations = pstmt.executeUpdate();
            pstmt.close();
            if (classRegistrations >= FitnessClass.getCapacity(conn, classId)) return false;

            // Prevent a member from registering for a conflicting class.
            if (isConflicting(
                conn,
                memberId,
                FitnessClass.getStart(conn, classId),
                FitnessClass.getEnd(conn, classId)
            ))
                return false;
            query = """
                INSERT INTO class_registration (
                    class_id,
                    member_id,
                    register_date
                ) VALUES (?, ?)
                """;
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, classId);
            pstmt.setInt(2, memberId);
            pstmt.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Delete a class registration by ID.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @param memberId The ID of the member.
     * @return True if successfully deleted, false otherwise.
     */
    public static boolean delete(Connection conn, Integer classId, Integer memberId) {
        try {
            String query = """
                DELETE FROM class_registration
                    WHERE class_id = ?
                        AND member_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, classId);
            pstmt.setInt(2, memberId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Check if a time slot conflicts with an existing class registration.
     * @param conn The connection to the database.
     * @param memberId The ID of the member with the schedule to be checked.
     * @param startTimestamp The starting timestamp of the time slot.
     * @param endTimestamp The end timestamp of the time slot.
     * @return True if there exists a conflict or failed to find a match for the ID,
     *  false otherwise.
     */
    public static boolean isConflicting(
        Connection conn,
        Integer memberId,
        Timestamp startTimestamp,
        Timestamp endTimestamp
    ) {
        try {
            // Get all the classes of the trainer.
            String query = """
                SELECT start_timestamp, end_timestamp
                    FROM class_registration
                    WHERE member_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery(query);

            // Check each class for scheduling conflicts.
            while(rs.next()) {
                Timestamp st = rs.getTimestamp("start_timestamp");
                Timestamp et = rs.getTimestamp("end_timestamp");
                if (Utilities.overlaps(startTimestamp, endTimestamp, st, et))
                    return true;
            }

        } catch (Exception e) {
            Terminal.exception(e);
            return true;
        }
        return false;
    }
}