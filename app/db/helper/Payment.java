import java.sql.*;
import java.util.LinkedList;

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
     * Get the invoice ID of a payment record by ID.
     * @param conn The connection to the database.
     * @param paymentId The ID of the payment record.
     * @return The invoice ID of the payment record.
     */
    public static Integer getInvoiceId(Connection conn, Integer paymentId) {
        try {
            if (!exists(conn, paymentId)) return null;
            String query = "SELECT invoice_id FROM payments WHERE payment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, paymentId);
            Integer id = pstmt.executeQuery(query).getInt("invoice_id");
            pstmt.close();
            return id;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the amount paid in a payment record by ID.
     * @param conn The connection to the database.
     * @param paymentId The ID of the payment record.
     * @return The amount paid in the payment record.
     */
    public static Float getAmountPaid(Connection conn, Integer paymentId) {
        try {
            if (!exists(conn, paymentId)) return null;
            String query = "SELECT amount_paid FROM payments WHERE payment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, paymentId);
            Float amount = pstmt.executeQuery(query).getFloat("amount_paid");
            pstmt.close();
            return amount;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the payment method of a payment record by ID.
     * @param conn The connection to the database.
     * @param paymentId The ID of the payment record.
     * @return The payment method of the payment record.
     */
    public static String getMethod(Connection conn, Integer paymentId) {
        try {
            if (!exists(conn, paymentId)) return null;
            String query = "SELECT method FROM payments WHERE payment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, paymentId);
            String method = pstmt.executeQuery(query).getString("method");
            pstmt.close();
            return method;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the payment date of a payment record by ID.
     * @param conn The connection to the database.
     * @param paymentId The ID of the payment record.
     * @return The payment date of the payment record.
     */
    public static Date getPaymentDate(Connection conn, Integer paymentId) {
        try {
            if (!exists(conn, paymentId)) return null;
            String query = "SELECT payment_date FROM payments WHERE payment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, paymentId);
            Date date = pstmt.executeQuery(query).getDate("payment_date");
            pstmt.close();
            return date;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the IDs of payment records of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The IDs of the payment records.
     */
    public static LinkedList<Integer> getRecords(Connection conn, Integer memberId) {
        try {
            String query = "SELECT payment_id FROM payments WHERE member_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery(query);
            LinkedList<Integer> ids = new LinkedList<>();
            while (rs.next()) ids.add(rs.getInt("payment_id"));
            pstmt.close();
            rs.close();
            return ids;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if a payment record exists with the given ID.
     * @param conn The connection to the database.
     * @param paymentId The ID of the payment record.
     * @return True if the payment record exists, false otherwise.
     */
    public static boolean exists(Connection conn, Integer paymentId) {
        try {
            String query = "SELECT payment_id FROM payments WHERE payment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, paymentId);
            boolean exists = pstmt.executeQuery(query).next();
            pstmt.close();
            return exists;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }
}
