import java.sql.*;
import java.util.LinkedList;

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
            if (!(isRegistered(conn, classId, memberId))) return false;
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
            ResultSet rs = pstmt.executeQuery();

            // Check each class for scheduling conflicts.
            while(rs.next()) {
                Timestamp st = rs.getTimestamp("start_timestamp");
                Timestamp et = rs.getTimestamp("end_timestamp");
                if (Utilities.overlaps(startTimestamp, endTimestamp, st, et))
                    return true;
            }
            
            rs.close();

        } catch (Exception e) {
            Terminal.exception(e);
            return true;
        }
        return false;
    }

    /**
     * Check if a member is registers in a class with the given IDs.
     * @param conn The connection to the database.
     * @param classId The ID of class.
     * @param memberId The ID of the member.
     * @return True if the member is registered in the class, false otherwise.
     */
    public static boolean isRegistered(Connection conn, Integer classId, Integer memberId) {
        try {
            String query = """
                SELECT *
                    FROM class_registration
                    WHERE class_id = ?
                        AND member_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, classId);
            pstmt.setInt(2, memberId);
            ResultSet rs = pstmt.executeQuery();
            boolean isRegistered = rs.next();
            pstmt.close();
            rs.close();
            return isRegistered;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }

    /**
     * Get the date a member registered to a class.
     * @param conn The connection to the database.
     * @param classId The ID of class.
     * @param memberId The ID of the member.
     * @return The registration date of the member registering to the class.
     */
    public static Date getRegistrationDate(Connection conn, Integer classId, Integer memberId) {
        try {
            if (!isRegistered(conn, classId, memberId)) return null;
            String query = """
                SELECT register_date
                    FROM class_registration
                    WHERE class_id = ?
                        AND member_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, classId);
            pstmt.setInt(2, memberId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            Date date = rs.getDate("register_date");
            pstmt.close();
            rs.close();
            return date;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the IDs of registered classes of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The IDs of the registered classes.
     */
    public static LinkedList<Integer> getRegistratedClasses(Connection conn, Integer memberId) {
        try {
            String query = "SELECT class_id FROM class_registration WHERE member_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            LinkedList<Integer> ids = new LinkedList<>();
            while (rs.next()) ids.add(rs.getInt("class_id"));
            pstmt.close();
            rs.close();
            return ids;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the IDs of registered students of a class by ID.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @return The IDs of the registered members.
     */
    public static LinkedList<Integer> getRegisteredMembers(Connection conn, Integer classId) {
        try {
            String query = "SELECT member_id FROM class_registration WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, classId);
            ResultSet rs = pstmt.executeQuery();
            LinkedList<Integer> ids = new LinkedList<>();
            while (rs.next()) ids.add(rs.getInt("member_id"));
            pstmt.close();
            rs.close();
            return ids;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }
}