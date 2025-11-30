import java.sql.*;

/**
 * Helper class to handle the classes table in the database.
 */
public class FitnessClass {
    
    /**
     * Insert a class into the classes table.
     * @param conn The connection to the database.
     * @param trainerId The ID of the trainer.
     * @param roomId The ID of the room.
     * @param capacity The capacity of the class.
     * @param startTimestamp The starting timestamp of the session.
     * @param endTimestamp The end timestamp of the session.
     * @return True if successfully added, false otherwise.
     */
    public static boolean add(
        Connection conn,
        Integer trainerId,
        Integer roomId,
        Integer capacity,
        Timestamp startTimestamp,
        Timestamp endTimestamp
    ) {
        try {
            String query = """
                INSERT INTO classes (
                    trainer_id,
                    room_id,
                    capacity
                    start_timestamp,
                    end_timestamp
                ) VALUES (?, ?, ?, ?, ?)
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            pstmt.setInt(2, roomId);
            pstmt.setInt(3, capacity);
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
     * Update the trainer of a class.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @param trainerId The trainer to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateTrainer(Connection conn, Integer classId, Integer trainerId) {
        try {
            String query = "UPDATE classes SET trainer_id = ? WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            pstmt.setInt(2, classId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Update the room of a class.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @param roomId The ID of the room to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateRoom(Connection conn, Integer classId, Integer roomId) {
        try {
            String query = "UPDATE classes SET room_id = ? WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, roomId);
            pstmt.setInt(2, classId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Update the starting timestamp of a class.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @param ts The timestamp to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateStart(Connection conn, Integer classId, Timestamp ts) {
        try {
            String query = "UPDATE classes SET start_timestamp = ? WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setTimestamp(1, ts);
            pstmt.setInt(2, classId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Update the ending timestamp of a member.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @param ts The timestamp to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateEnd(Connection conn, Integer classId, Timestamp ts) {
        try {
            String query = "UPDATE classes SET end_timestamp = ? WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setTimestamp(1, ts);
            pstmt.setInt(2, classId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Delete a class by ID.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @return True if successfully deleted, false otherwise.
     */
    public static boolean delete(Connection conn, Integer classId) {
        try {
            String query = "DELETE FROM classes WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, classId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }
}
