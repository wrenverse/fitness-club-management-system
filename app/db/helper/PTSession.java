import java.sql.*;

/**
 * Helper class to handle the personal training sessions table in the database.
 */
public class PTSession {
    
    /**
     * Insert a personal training session registration into the personal training sessions table.
     * @param conn The connection to the database.
     * @param trainerId The ID of the trainer.
     * @param memberId The ID of the member.
     * @param roomId The ID of the room.
     * @param startTimestamp The starting timestamp of the session.
     * @param endTimestamp The end timestamp of the session.
     * @return True if successfully added, false otherwise.
     */
    public static boolean add(
        Connection conn,
        Integer trainerId,
        Integer memberId,
        Integer roomId,
        Timestamp startTimestamp,
        Timestamp endTimestamp
    ) {
        // Prevent a member from registering for a conflicting PT session.
        if (isConflicting(conn, memberId, Role.MEMBER, startTimestamp, endTimestamp))
            return false;
        try {
            String query = """
                INSERT INTO pt_sessions (
                    trainer_id,
                    member_id,
                    room_id,
                    start_timestamp,
                    end_timestamp
                ) VALUES (?, ?, ?, ?, ?)
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            pstmt.setInt(2, memberId);
            pstmt.setInt(3, roomId);
            pstmt.setTimestamp(4, startTimestamp);
            pstmt.setTimestamp(5, endTimestamp);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Delete a registered personal training session by ID.
     * @param conn The connection to the database.
     * @param sessionId The ID of the session.
     * @return True if successfully deleted, false otherwise.
     */
    public static boolean delete(Connection conn, Integer sessionId) {
        try {
            String query = "DELETE FROM pt_sessions WHERE session_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, sessionId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Check if a time slot conflicts with existing time slots.
     * @param conn The connection to the database.
     * @param id The ID of the trainer or member with the schedule to be checked.
     * @param role The role of the person being checked.
     * @param startTimestamp The starting timestamp of the time slot.
     * @param endTimestamp The end timestamp of the time slot.
     * @return True if there exists a conflict or failed to find a match for the ID,
     *  false otherwise.
     */
    public static boolean isConflicting(
        Connection conn,
        Integer id,
        Role role,
        Timestamp startTimestamp,
        Timestamp endTimestamp
    ) {
        try {
            // Get all the personal training sessions of the member or trainer.
            String query;
            if (role == Role.MEMBER) 
                query = """
                    SELECT start_timestamp, end_timestamp
                        FROM pt_sessions
                        WHERE member_id = ?
                    """;
            else query = """
                    SELECT start_timestamp, end_timestamp
                        FROM pt_sessions
                        WHERE trainer_id = ?
                    """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery(query);

            // Check each personal training session for scheduling conflicts.
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
