import java.sql.*;

public class Payment {

    // Insert a payment record into the payments table.
    public static boolean addRecord(
        Connection conn,
        Integer invoiceId,
        Date date,
        Float amount,
        String method
    ) {
        try {
            String query = "INSERT INTO payments (invoice_id, date, " 
                + "amount, method) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, invoiceId);
            pstmt.setDate(2, date);
            pstmt.setFloat(3, amount);
            pstmt.setString(4, method);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }
}
