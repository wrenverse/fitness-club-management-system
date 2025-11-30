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
}
