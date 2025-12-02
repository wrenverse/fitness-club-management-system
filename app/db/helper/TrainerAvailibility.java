import java.sql.*;
import java.util.Calendar;
import java.util.LinkedList;

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
            // Prevent a trainer from creating a conflicting availibility block.
            if (isConflicting(conn, trainerId, startTimestamp, endTimestamp))
                return false;
            Calendar calendar = Calendar.getInstance();
            Timestamp st = new Timestamp(startTimestamp.getTime());
            Timestamp et = new Timestamp(endTimestamp.getTime());
            for (int i = 0; i < reccurences; i++) {
                // Add a week to the starting timestamp.
                calendar.setTime(st);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                st = new Timestamp(calendar.getTimeInMillis());
                // Add a week to the ending timestamp.
                calendar.setTime(et);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                et = new Timestamp(calendar.getTimeInMillis());
                if (isConflicting(conn, trainerId, st, et))
                    return false;
            }

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

    /**
     * Update the starting timestamp of an availibility block.
     * @param conn The connection to the database.
     * @param availibilityId The ID of the availibility block.
     * @param st The timestamp to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateStart(Connection conn, Integer availibilityId, Timestamp st) {
        try {
            String query = """
                UPDATE trainer_availibility
                    SET start_timestamp = ?
                    WHERE availibility_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setTimestamp(1, st);
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
     * @param st The timestamp to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateEnd(Connection conn, Integer availibilityId, Timestamp st) {
        try {
            String query = """
                UPDATE trainer_availibility
                    SET end_timestamp = ?
                    WHERE availibility_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setTimestamp(1, st);
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
     * Get the IDs of available personal training sessions of a trainer by ID.
     * @param conn The connection to the database.
     * @param trainerId The ID of the trainer.
     * @return The IDs of the available personal training sessions.
     */
    public static LinkedList<Integer> getAvailibilities(Connection conn, Integer trainerId) {
        try {
            String query = "SELECT availibility_id FROM trainer_availibility WHERE trainer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            ResultSet rs = pstmt.executeQuery();
            LinkedList<Integer> ids = new LinkedList<>();
            while (rs.next()) ids.add(rs.getInt("availibility_id"));
            pstmt.close();
            rs.close();
            return ids;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if a time slot conflicts with an existing availibility.
     * @param conn The connection to the database.
     * @param trainerId The ID of the trainer with the schedule to be checked.
     * @param startTimestamp The starting timestamp of the time slot.
     * @param endTimestamp The end timestamp of the time slot.
     * @return True if there exists a conflict or failed to find a match for the ID,
     *  false otherwise.
     */
    public static boolean isConflicting(
        Connection conn,
        Integer trainerId,
        Timestamp startTimestamp,
        Timestamp endTimestamp
    ) {
        try {
            // Get all the classes of the trainer.
            String query = """
                SELECT start_timestamp, end_timestamp
                    FROM classes
                    WHERE trainer_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            ResultSet rs = pstmt.executeQuery();

            // Check each availibility block for scheduling conflicts.
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
}
