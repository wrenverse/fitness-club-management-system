import java.sql.*;

public class Trainer {
    
    public static ResultSet getInfo(Connection conn, Integer trainerId) {
        try {
            String query = "SELECT * FROM trainers WHERE trainer_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            return pstmt.executeQuery(query);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }
}
