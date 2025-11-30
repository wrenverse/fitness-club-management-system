import java.sql.*;

/**
 * Helper class to handle the members table in the database.
 */
public class Member {
    
    /**
     * Insert a member into the members table.
     * Automatically records the current date as the join date.
     * @param conn The connection to the database.
     * @param name The full name of the member.
     * @param dob The date of birth of the member.
     * @param gender The gender of the member.
     * @param email The email of the member.
     * @param phone The phone of the member.
     * @return True if successfully added, false otherwise.
     */
    public static boolean add(
        Connection conn,
        String name,
        Date dob,
        String gender,
        String email,
        String phone
    ) {
        try {
            String query = """
                INSERT INTO members (
                    name,
                    date_of_birth,
                    gender,
                    email,
                    phone,
                    join_date
                ) VALUES (?, ?, ?, ?, ?, ?)
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setDate(2, dob);
            pstmt.setString(3, gender);
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            pstmt.setDate(6, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Update the name of a member.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @param name The name to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateMember(Connection conn, Integer memberId, String name) {
        try {
            String query = "UPDATE members SET name = ? WHERE member_id = ?";
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

    /**
     * Update the gender of a member.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @param gender The gender to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateGender(Connection conn, Integer memberId, String gender) {
        try {
            String query = "UPDATE members SET gender = ? WHERE member_id = ?";
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

    /**
     * Update the date of birth of a member.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @param date The date to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateDOB(Connection conn, Integer memberId, Date date) {
        try {
            String query = "UPDATE members SET date_of_birth = ? WHERE member_id = ?";
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

    /**
     * Update the email of a member.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @param email The email to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateEmail(Connection conn, Integer memberId, String email) {
        try {
            String query = "UPDATE members SET email = ? WHERE member_id = ?";
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

    /**
     * Update the phone number of a member.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @param phone The phone number to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updatePhone(Connection conn, Integer memberId, String phone) {
        try {
            String query = "UPDATE members SET phone = ? WHERE member_id = ?";
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

    /**
     * Get the information of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The table row containing the information.
     */
    public static ResultSet getInfo(Connection conn, Integer memberId) {
        try {
            String query = "SELECT * FROM members WHERE member_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            return pstmt.executeQuery(query);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the information of a member by name.
     * @param conn The connection to the database.
     * @param name The name of the member(s).
     * @return The table rows containing the information.
     */
    public static ResultSet getInfo(Connection conn, String name) {
        try {
            String query = "SELECT * FROM members WHERE LOWER(name) = LOWER(?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            return pstmt.executeQuery(query);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }
}
