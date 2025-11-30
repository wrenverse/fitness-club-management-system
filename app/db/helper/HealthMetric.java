import java.sql.*;

/**
 * Helper class to handle the health metrics table in the database.
 */
public class HealthMetric {
    
    /**
     * Insert a health metric record into the health metrics table.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @param timestamp The timestamp of the record.
     * @param heartRate The heart rate of the member recorded in beats per minute.
     * @param bodyFat The body fat of the member recorded in percentage.
     * @param weight The weight of the member recorded in pounds.
     * @param height The height of the member recorded in centimeters.
     * @return True if successfully added, false otherwise.
     */
    public static boolean add(
        Connection conn,
        Integer memberId,
        Timestamp timestamp,
        Integer heartRate,
        Float bodyFat,
        Integer weight,
        Integer height
    ) {
        try {
            String query = """
                INSERT INTO health_metrics (
                    member_id,
                    timestamp,
                    heart_rate,
                    body_fat,
                    weight,
                    height
                ) VALUES (?, ?, ?, ?, ?, ?)
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            pstmt.setTimestamp(2, timestamp);
            pstmt.setInt(3, heartRate);
            pstmt.setFloat(4, bodyFat);
            pstmt.setInt(5, weight);
            pstmt.setInt(6, height);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Get the latest recorded health metric record of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The table row containing the latest record.
     */
    public static ResultSet getLatest(Connection conn, Integer memberId) {
        try {
            String query = """
                SELECT * FROM health_metrics
                    WHERE timestamp =
                        (SELECT MAX(timestamp)
                            FROM health_metrics
                            WHERE member_id = ?)
                        AND member_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, memberId);
            return pstmt.executeQuery(query);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the health metric records of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The table rows containing the records.
     */
    public static ResultSet getRecords(Connection conn, Integer memberId) {
        try {
            String query = "SELECT * FROM health_metrics WHERE member_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            return pstmt.executeQuery(query);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }
}
