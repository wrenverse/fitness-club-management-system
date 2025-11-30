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
            String query = """
                INSERT INTO class_registration (
                    class_id,
                    member_id,
                    register_date
                ) VALUES (?, ?)
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
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
}
