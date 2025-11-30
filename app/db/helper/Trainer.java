import java.sql.*;

/**
 * Helper class to handle the trainers table in the database.
 */
public class Trainer {
    
    /**
     * Get the information of a trainer by ID.
     * @param conn The connection to the database.
     * @param trainerId The ID of the trainer.
     * @return The table row containing the information.
     */
    public static ResultSet getInfo(Connection conn, Integer trainerId) {
        try {
            String query = "SELECT * FROM trainers WHERE trainer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            return pstmt.executeQuery(query);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }
}
