import java.sql.*;

public class Members {
    
    // Insert a member into the members table.
    public static boolean addMember(
        Connection conn, String name, String gender, Date dob, String email, String phone
    ) {
        try {
            String query = "INSERT INTO members (name, gender, date_of_birth, email, phone) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, gender);
            pstmt.setDate(3, dob);
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    // Update a member's name.
    public static boolean updateMember(Connection conn, Integer memberId, String name) {
        try {
            String query = "UPDATE members SET name=? WHERE member_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setInt(2, memberId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    // Update a member's gender.
    public static boolean updateGender(Connection conn, Integer memberId, String gender) {
        try {
            String query = "UPDATE members SET gender=? WHERE member_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, gender);
            pstmt.setInt(2, memberId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    // Update a member's date of birth.
    public static boolean updateDOB(Connection conn, Integer memberId, Date date) {
        try {
            String query = "UPDATE members SET date_of_birth=? WHERE member_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, date);
            pstmt.setInt(2, memberId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    // Update a member's email.
    public static boolean updateEmail(Connection conn, Integer memberId, String email) {
        try {
            String query = "UPDATE members SET email=? WHERE member_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setInt(2, memberId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    // Update a member's phone number.
    public static boolean updatePhone(Connection conn, Integer memberId, String phone) {
        try {
            String query = "UPDATE members SET phone=? WHERE member_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, phone);
            pstmt.setInt(2, memberId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    // Get a member's information.
    public static ResultSet getInfo(Connection conn, Integer memberId) {
        try {
            String query = "SELECT * FROM members WHERE timestamp="
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

    // Get a member's information by name.
    public static ResultSet getInfo(Connection conn, String name) {
        try {
            String query = "SELECT * FROM members WHERE LOWER(name)=LOWER(?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            return pstmt.executeQuery(query);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }
}
