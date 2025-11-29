import java.sql.*;

public class HealthMetrics {
    
    // Insert a health metric record into the health metrics table.
    public static boolean addRecord(
        Connection conn,
        Integer memberId,
        Integer heartRate,
        Float bodyFat,
        Integer weight,
        Integer height,
        Timestamp timestamp
    ) {
        try {
            String query = "INSERT INTO health_metrics (member_id, heartRate, " 
                + "bodyFat, weight, height, timestamp) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, heartRate);
            pstmt.setFloat(3, bodyFat);
            pstmt.setInt(4, weight);
            pstmt.setInt(5, height);
            pstmt.setTimestamp(6, timestamp);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    // Get a member's latest recorded health metric record.
    public static ResultSet getLatest(Connection conn, Integer memberId) {
        try {
            String query = "SELECT * FROM health_metrics WHERE timestamp = "
                + "(SELECT MAX(timestamp) FROM health_metrics WHERE member_id=?"
                + ") AND member_id=?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, memberId);
            return pstmt.executeQuery(query);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    // Get a member's health metric records.
    public static ResultSet getRecords(Connection conn, Integer memberId) {
        try {
            String query = "SELECT * FROM health_metrics WHERE member_id=?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            return pstmt.executeQuery(query);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }
}
