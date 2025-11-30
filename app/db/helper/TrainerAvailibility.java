import java.sql.*;
import java.util.Calendar;

/**
 * Helper class to handle the trainer availibilty table in the database.
 */
public class TrainerAvailibility {
    
    /**
     * Insert a trainer availibility block into the trainer availibility table.
     * @param conn The connection to the database.
     * @param trainerId The ID of the trainer.
     * @param startTimestamp The starting timestamp of the block.
     * @param endTimestamp The end timestamp of the block.
     * @param reccurences The number of weekly recurrences of the block.
     * @return True if successfully added, false otherwise.
     */
    public static boolean add(
        Connection conn,
        Integer trainerId,
        Timestamp startTimestamp,
        Timestamp endTimestamp,
        Integer reccurences
    ) {
        try {
            Calendar calendar = Calendar.getInstance();
            String query = """
                INSERT INTO trainer_availibility (
                    trainer_id,
                    start_timestamp,
                    end_timestamp,
                    recurrences
                ) VALUES (?, ?, ?, ?)
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            pstmt.setTimestamp(2, startTimestamp);
            pstmt.setTimestamp(3, endTimestamp);
            pstmt.executeUpdate();
            // Insert additional reccuring instances of the block to the table.
            for (int i = 0; i < reccurences; i++) {
                // Add a week to the starting timestamp.
                calendar.setTime(startTimestamp);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                startTimestamp = new Timestamp(calendar.getTimeInMillis());
                // Add a week to the ending timestamp.
                calendar.setTime(endTimestamp);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                endTimestamp = new Timestamp(calendar.getTimeInMillis());
                pstmt.setTimestamp(2, startTimestamp);
                pstmt.setTimestamp(3, endTimestamp);
                pstmt.executeUpdate();
            }
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Update the starting timestamp of an availibility block.
     * @param conn The connection to the database.
     * @param availibilityId The ID of the availibility block.
     * @param ts The timestamp to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateStart(Connection conn, Integer availibilityId, Timestamp ts) {
        try {
            String query = """
                UPDATE trainer_availibility
                    SET start_timestamp = ?
                    WHERE availibility_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setTimestamp(1, ts);
            pstmt.setInt(2, availibilityId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Update the ending timestamp of an availibility block.
     * @param conn The connection to the database.
     * @param availibilityId The ID of the availibility block.
     * @param ts The timestamp to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateEnd(Connection conn, Integer availibilityId, Timestamp ts) {
        try {
            String query = """
                UPDATE trainer_availibility
                    SET end_timestamp = ?
                    WHERE availibility_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setTimestamp(1, ts);
            pstmt.setInt(2, availibilityId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Delete an availibility block by ID.
     * @param conn The connection to the database.
     * @param availibilityId The ID of the availibility block.
     * @return True if successfully deleted, false otherwise.
     */
    public static boolean delete(Connection conn, Integer availibilityId) {
        try {
            String query = "DELETE FROM trainer_availibility WHERE availibility_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, availibilityId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }
}
