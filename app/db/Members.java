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
            pstmt.setString(1, phone);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    // Update a member's name in the members table.
    public static boolean updateMember(Connection conn, Integer member_id, String name) {
        try {
            String query = "UPDATE members SET name=? WHERE member_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setInt(2, member_id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    // Update a member's gender in the members table.
    public static boolean updateGender(Connection conn, Integer member_id, String gender) {
        try {
            String query = "UPDATE members SET gender=? WHERE member_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, gender);
            pstmt.setInt(2, member_id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    // Update a member's date of birth in the members table.
    public static boolean updateDOB(Connection conn, Integer member_id, Date date) {
        try {
            String query = "UPDATE members SET date_of_birth=? WHERE member_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, date);
            pstmt.setInt(2, member_id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    // Update a member's email in the members table.
    public static boolean updateEmail(Connection conn, Integer member_id, String email) {
        try {
            String query = "UPDATE members SET email=? WHERE member_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setInt(2, member_id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    // Update a member's phone number in the members table.
    public static boolean updatePhone(Connection conn, Integer member_id, String phone) {
        try {
            String query = "UPDATE members SET phone=? WHERE member_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, phone);
            pstmt.setInt(2, member_id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }
}
