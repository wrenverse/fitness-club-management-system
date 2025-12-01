import java.sql.*;

/**
 * Helper class to handle the trainers table in the database.
 */
public class Trainer {

    /**
     * Get the name of a trainer by ID.
     * @param conn The connection to the database.
     * @param trainerId The ID of the trainer.
     * @return The name of the trainer.
     */
    public static String getName(Connection conn, Integer trainerId) {
        try {
            if (!exists(conn, trainerId)) return null;
            String query = "SELECT name FROM trainers WHERE trainer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            return pstmt.executeQuery(query).getString("name");
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the email of a trainer by ID.
     * @param conn The connection to the database.
     * @param trainerId The ID of the trainer.
     * @return The email of the trainer.
     */
    public static String getEmail(Connection conn, Integer trainerId) {
        try {
            if (!exists(conn, trainerId)) return null;
            String query = "SELECT email FROM trainers WHERE trainer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            return pstmt.executeQuery(query).getString("email");
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the phone number of a trainer by ID.
     * @param conn The connection to the database.
     * @param trainerId The ID of the trainer.
     * @return The phone number of the trainer.
     */
    public static String getPhone(Connection conn, Integer trainerId) {
        try {
            if (!exists(conn, trainerId)) return null;
            String query = "SELECT phone FROM trainers WHERE trainer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            return pstmt.executeQuery(query).getString("phone");
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the specialization of a trainer by ID.
     * @param conn The connection to the database.
     * @param trainerId The ID of the trainer.
     * @return The specialization of the trainer.
     */
    public static String getSpecialization(Connection conn, Integer trainerId) {
        try {
            if (!exists(conn, trainerId)) return null;
            String query = "SELECT specialization FROM trainers WHERE trainer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            return pstmt.executeQuery(query).getString("specialization");
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the hire date of a trainer by ID.
     * @param conn The connection to the database.
     * @param trainerId The ID of the trainer.
     * @return The hire date of the trainer.
     */
    public static Date getHireDate(Connection conn, Integer trainerId) {
        try {
            if (!exists(conn, trainerId)) return null;
            String query = "SELECT hire_date FROM trainers WHERE trainer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            return pstmt.executeQuery(query).getDate("hire_date");
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if a trainer exists with the given ID.
     * @param conn The connection to the database.
     * @param trainerId The ID of the trainer.
     * @return True if the trainer exists, false otherwise.
     */
    public static boolean exists(Connection conn, Integer trainerId) {
        try {
            String query = "SELECT trainer_id FROM trainers WHERE trainer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            return pstmt.executeQuery(query).next();
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }
}
