import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Helper class to handle the health metrics table in the database.
 */
public class HealthMetric {
    
    /**
     * Insert a health metric record into the health metrics table.
     * Automatically records the current timestamp as the timestamp of the record.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @param heartRate The heart rate of the member recorded in beats per minute.
     * @param bodyFat The body fat of the member recorded in percentage.
     * @param weight The weight of the member recorded in pounds.
     * @param height The height of the member recorded in centimeters.
     * @return True if successfully added, false otherwise.
     */
    public static boolean add(
        Connection conn,
        Integer memberId,
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
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));
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
     * Get the timestamp of a health metric record by ID.
     * @param conn The connection to the database.
     * @param metricId The ID of the health metric record.
     * @return The timestamp of the record.
     */
    public static Timestamp getTimestamp(Connection conn, Integer metricId) {
        try {
            if (!exists(conn, metricId)) return null;
            String query = "SELECT timestamp FROM health_metrics WHERE metric_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, metricId);
            Timestamp ts = pstmt.executeQuery(query).getTimestamp("timestamp");
            pstmt.close();
            return ts;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the heart rate of a health metric record by ID.
     * @param conn The connection to the database.
     * @param metricId The ID of the health metric record.
     * @return The heart rate of the record.
     */
    public static Integer getHeartRate(Connection conn, Integer metricId) {
        try {
            if (!exists(conn, metricId)) return null;
            String query = "SELECT heart_rate FROM health_metrics WHERE metric_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, metricId);
            Integer hr = pstmt.executeQuery(query).getInt("heart_rate");
            pstmt.close();
            return hr;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the body fat percentage of a health metric record by ID.
     * @param conn The connection to the database.
     * @param metricId The ID of the health metric record.
     * @return The body fat percentage of the record.
     */
    public static Float getBodyFat(Connection conn, Integer metricId) {
        try {
            if (!exists(conn, metricId)) return null;
            String query = "SELECT body_fat FROM health_metrics WHERE metric_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, metricId);
            Float bfp = pstmt.executeQuery(query).getFloat("body_fat");
            pstmt.close();
            return bfp;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the weight of a health metric record by ID.
     * @param conn The connection to the database.
     * @param metricId The ID of the health metric record.
     * @return The weight of the record.
     */
    public static Integer getWeight(Connection conn, Integer metricId) {
        try {
            if (!exists(conn, metricId)) return null;
            String query = "SELECT weight FROM health_metrics WHERE metric_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, metricId);
            Integer weight = pstmt.executeQuery(query).getInt("weight");
            pstmt.close();
            return weight;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the height of a health metric record by ID.
     * @param conn The connection to the database.
     * @param metricId The ID of the health metric record.
     * @return The height of the record.
     */
    public static Integer getHeight(Connection conn, Integer metricId) {
        try {
            if (!exists(conn, metricId)) return null;
            String query = "SELECT height FROM health_metrics WHERE metric_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, metricId);
            Integer height = pstmt.executeQuery(query).getInt("height");
            pstmt.close();
            return height;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the ID of the latest recorded health metric record of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The ID of the the latest record.
     */
    public static Integer getLatest(Connection conn, Integer memberId) {
        try {
            String query = """
                SELECT metric_id FROM health_metrics
                    WHERE timestamp =
                        (SELECT MAX(timestamp)
                            FROM health_metrics
                            WHERE member_id = ?)
                        AND member_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, memberId);
            Integer id = pstmt.executeQuery(query).getInt("metric_id");
            pstmt.close();
            return id;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the IDs of health metrics records of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The IDs of the health metric records.
     */
    public static LinkedList<Integer> getRecords(Connection conn, Integer memberId) {
        try {
            String query = "SELECT metric_id FROM health_metrics WHERE member_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery(query);
            LinkedList<Integer> ids = new LinkedList<>();
            while (rs.next()) ids.add(rs.getInt("metric_id"));
            pstmt.close();
            rs.close();
            return ids;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if a health metric record exists with the given ID.
     * @param conn The connection to the database.
     * @param metricId The ID of the health metric record.
     * @return True if the record exists, false otherwise.
     */
    public static boolean exists(Connection conn, Integer metricId) {
        try {
            String query = "SELECT metric_id FROM health_metrics WHERE metric_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, metricId);
            boolean exists = pstmt.executeQuery(query).next();
            pstmt.close();
            return exists;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }
}
