import java.sql.*;

/**
 * Helper class to handle the payments table in the database.
 */
public class Payment {

    /**
     * Insert a payment record into the payments table.
     * Automatically records the current date as the payment date.
     * @param conn The connection to the database.
     * @param invoiceId The ID of the invoice.
     * @param amountPaid The amount of money paid.
     * @param method The method used for the payment.
     * @return True if successfully added, false otherwise.
     */
    public static boolean add(
        Connection conn,
        Integer invoiceId,
        Float amountPaid,
        String method
    ) {
        try {
            String query = """
                INSERT INTO payments (
                    invoice_id,
                    amount_paid,
                    method,
                    payment_date
                ) VALUES (?, ?, ?, ?)
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, invoiceId);
            pstmt.setFloat(2, amountPaid);
            pstmt.setString(3, method);
            pstmt.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Get the payment records of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The table rows containing the records.
     */
    public static ResultSet getRecords(Connection conn, Integer memberId) {
        try {
            String query = "SELECT * FROM payments WHERE member_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            return pstmt.executeQuery(query);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }
}
