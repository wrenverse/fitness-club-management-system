import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Helper class to handle the invoice table in the database.
 */
public class Invoice {
    
    /**
     * Insert an invoice into the invoices table.
     * Automatically records the current timestamp as the timestamp of the record.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @param total The total amount of the invoice.
     * @return True if successfully added, false otherwise.
     */
    public static boolean add(
        Connection conn,
        Integer memberId,
        Float total
    ) {
        try {
            String query = """
                INSERT INTO invoices (
                    member_id,
                    issue_timestamp,
                    total,
                    is_paid
                ) VALUES (?, ?, ?, ?)
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setFloat(3, total);
            pstmt.setBoolean(4, false);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Get the timestamp of an invoice by ID.
     * @param conn The connection to the database.
     * @param invoiceId The ID of the invoice record.
     * @return The timestamp of the invoice.
     */
    public static Timestamp getTimestamp(Connection conn, Integer invoiceId) {
        try {
            if (!exists(conn, invoiceId)) return null;
            String query = "SELECT issue_timestamp FROM invoices WHERE invoice_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            Timestamp ts = rs.getTimestamp("issue_timestamp");
            pstmt.close();
            rs.close();
            return ts;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the total of an invoice by ID.
     * @param conn The connection to the database.
     * @param invoiceId The ID of the invoice.
     * @return The total of the invoice.
     */
    public static Float getTotal(Connection conn, Integer invoiceId) {
        try {
            if (!exists(conn, invoiceId)) return null;
            String query = "SELECT total FROM invoices WHERE invoice_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            Float total = rs.getFloat("total");
            pstmt.close();
            rs.close();
            return total;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the IDs of invoices of a member by ID.
     * @param conn The connection to the database.
     * @param memberId The ID of the member.
     * @return The IDs of the invoices.
     */
    public static LinkedList<Integer> getInvoices(Connection conn, Integer memberId) {
        try {
            String query = "SELECT invoice_id FROM invoices WHERE member_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            LinkedList<Integer> ids = new LinkedList<>();
            while (rs.next()) ids.add(rs.getInt("invoice_id"));
            pstmt.close();
            rs.close();
            return ids;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if an invoice with the given ID is paid.
     * @param conn The connection to the database.
     * @param invoiceId The ID of invoice.
     * @return True if the invoice is paid, false otherwise.
     */
    public static boolean isPaid(Connection conn, Integer invoiceId) {
        try {
            if (!exists(conn, invoiceId)) return false;
            String query = "SELECT is_paid FROM invoices WHERE invoice_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            boolean isOperational = rs.getBoolean("is_paid");
            pstmt.close();
            rs.close();
            return isOperational;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }

    /**
     * Check if an invoice exists with the given ID.
     * @param conn The connection to the database.
     * @param invoiceId The ID of the invoice.
     * @return True if the invoice exists, false otherwise.
     */
    public static boolean exists(Connection conn, Integer invoiceId) {
        try {
            String query = "SELECT invoice_id FROM invoices WHERE invoice_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();
            pstmt.close();
            rs.close();
            return exists;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }
}
