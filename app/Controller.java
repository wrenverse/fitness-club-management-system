import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Controller to keep DB calls out of the CLI.
 */
public class Controller {

    private final Connection conn;

    public Controller(Connection conn) {
        this.conn = conn;
    }

    public boolean registerMember(
        String name,
        Date dob,
        String gender,
        String email,
        String phone
    ) {
        if (emailExists(email)) {
            Terminal.error("A member with that email already exists.");
            return false;
        }
        return Member.add(conn, name, dob, gender, email, phone);
    }

    public Integer getMemberIdByEmail(String email) {
        try {
            String query = "SELECT member_id FROM members WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            Integer id = null;
            if (rs.next()) id = rs.getInt("member_id");
            rs.close();
            pstmt.close();
            return id;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    private boolean emailExists(String email) {
        try {
            String query = "SELECT 1 FROM members WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            boolean exists = pstmt.executeQuery().next();
            pstmt.close();
            return exists;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }
}
