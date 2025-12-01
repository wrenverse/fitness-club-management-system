import java.sql.*;
import java.util.LinkedList;

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
    public static boolean updateName(Connection conn, Integer memberId, String name) {
        try {
            if (!(exists(conn, memberId))) return false;
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
            if (!(exists(conn, memberId))) return false;
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
            if (!(exists(conn, memberId))) return false;
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
            if (!(exists(conn, memberId))) return false;
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
            if (!(exists(conn, memberId))) return false;
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
     * Get the name of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The name of the member.
     */
    public static String getName(Connection conn, Integer memberId) {
        try {
            if (!exists(conn, memberId)) return null;
            String query = "SELECT name FROM members WHERE member_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            String name = pstmt.executeQuery(query).getString("name");
            pstmt.close();
            return name;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the date of birth of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The date of birth of the member.
     */
    public static Date getDOB(Connection conn, Integer memberId) {
        try {
            if (!exists(conn, memberId)) return null;
            String query = "SELECT date_of_birth FROM members WHERE member_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            Date dob = pstmt.executeQuery(query).getDate("name");
            pstmt.close();
            return dob;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the gender of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The gender of the member.
     */
    public static String getGender(Connection conn, Integer memberId) {
        try {
            if (!exists(conn, memberId)) return null;
            String query = "SELECT gender FROM members WHERE member_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            String gender = pstmt.executeQuery(query).getString("gender");
            pstmt.close();
            return gender;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the email of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The email of the member.
     */
    public static String getEmail(Connection conn, Integer memberId) {
        try {
            if (!exists(conn, memberId)) return null;
            String query = "SELECT email FROM members WHERE member_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            String email = pstmt.executeQuery(query).getString("email");
            pstmt.close();
            return email;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the phone of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The phone of the member.
     */
    public static String getPhone(Connection conn, Integer memberId) {
        try {
            if (!exists(conn, memberId)) return null;
            String query = "SELECT phone FROM members WHERE member_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            String phone = pstmt.executeQuery(query).getString("phone");
            pstmt.close();
            return phone;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the join date of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The join date of the member.
     */
    public static Date getJoinDate(Connection conn, Integer memberId) {
        try {
            if (!exists(conn, memberId)) return null;
            String query = "SELECT joine_date FROM members WHERE memberId = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            Date date = pstmt.executeQuery(query).getDate("join_date");
            pstmt.close();
            return date;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the IDs of members with the given name.
     * @param conn The connection to the database.
     * @param name The name to be searched.
     * @return The IDs of the matching members.
     */
    public static LinkedList<Integer> getMembers(Connection conn, String name) {
        try {
            String query = "SELECT member_id FROM members WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery(query);
            LinkedList<Integer> ids = new LinkedList<>();
            while (rs.next()) ids.add(rs.getInt("member_id"));
            pstmt.close();
            rs.close();
            return ids;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if a member exists with the given ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return True if the member exists, false otherwise.
     */
    public static boolean exists(Connection conn, Integer memberId) {
        try {
            String query = "SELECT member_id FROM members WHERE member_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            boolean exists = pstmt.executeQuery(query).next();
            pstmt.close();
            return exists;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }
}
