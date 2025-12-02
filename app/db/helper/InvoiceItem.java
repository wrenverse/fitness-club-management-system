import java.sql.*;
import java.util.LinkedList;

/**
 * Helper class to handle the invoice items table in the database.
 */
public class InvoiceItem {
    
    /**
     * Insert an invoice item into the invoice items table.
     * @param itemNum The quantity of an invoice item.
     * @param itemType The type of an invoice item.
     * @param quantity The quantity of an invoice item.
     * @param unitPrice The unit price of an invoice item.
     * @param totalPrice The total price of an invoice item.
     * @return True if successfully added, false otherwise.
     */
    public static boolean add(
        Connection conn,
        Integer itemNum,
        String itemType,
        Integer quantity,
        Float unitPrice,
        Float totalPrice
    ) {
        try {
            String query = """
                INSERT INTO health_metrics (
                    item_num,
                    item_type,
                    quantity,
                    unit_price,
                    total_price
                ) VALUES (?, ?, ?, ?, ?)
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, itemNum);
            pstmt.setString(2, itemType);
            pstmt.setInt(3, quantity);
            pstmt.setFloat(4, unitPrice);
            pstmt.setFloat(5, totalPrice);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Get the item type of an invoice item by ID.
     * @param conn The connection to the database.
     * @param invoiceId The ID of the invoice item.
     * @param itemNum The item number of the invoice item.
     * @return The item type of the invoice item.
     */
    public static String getItemType(Connection conn, Integer invoiceId, Integer itemNum) {
        try {
            if (!exists(conn, invoiceId, itemNum)) return null;
            String query = """
                SELECT item_type
                    FROM invoice_items
                    WHERE invoice_id = ?
                        AND item_num = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, invoiceId);
            pstmt.setInt(2, itemNum);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            String itemType = rs.getString("item_type");
            pstmt.close();
            rs.close();
            return itemType;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the quantity of an invoice item by ID.
     * @param conn The connection to the database.
     * @param invoiceId The ID of the invoice item.
     * @param itemNum The item number of the invoice item.
     * @return The quantity of the invoice item.
     */
    public static Integer getQuantity(Connection conn, Integer invoiceId, Integer itemNum) {
        try {
            if (!exists(conn, invoiceId, itemNum)) return null;
            String query = """
                SELECT quantity
                    FROM invoice_items
                    WHERE invoice_id = ?
                        AND item_num = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, invoiceId);
            pstmt.setInt(2, itemNum);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            Integer quantity = rs.getInt("quantity");
            pstmt.close();
            rs.close();
            return quantity;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the unit price of an invoice item by ID.
     * @param conn The connection to the database.
     * @param invoiceId The ID of the invoice item.
     * @param itemNum The item number of the invoice item.
     * @return The unit price of the invoice item.
     */
    public static Float getUnitPrice(Connection conn, Integer invoiceId, Integer itemNum) {
        try {
            if (!exists(conn, invoiceId, itemNum)) return null;
            String query = """
                SELECT unit_price
                    FROM invoice_items
                    WHERE invoice_id = ?
                        AND item_num = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, invoiceId);
            pstmt.setInt(2, itemNum);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            Float unitPrice = rs.getFloat("unit_price");
            pstmt.close();
            rs.close();
            return unitPrice;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the total price of an invoice item by ID.
     * @param conn The connection to the database.
     * @param invoiceId The ID of the invoice item.
     * @param itemNum The item number of the invoice item.
     * @return The total price of the invoice item.
     */
    public static Float getTotalPrice(Connection conn, Integer invoiceId, Integer itemNum) {
        try {
            if (!exists(conn, invoiceId, itemNum)) return null;
            String query = """
                SELECT total_price
                    FROM invoice_items
                    WHERE invoice_id = ?
                        AND item_num = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, invoiceId);
            pstmt.setInt(2, itemNum);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            Float unitPrice = rs.getFloat("total_price");
            pstmt.close();
            rs.close();
            return unitPrice;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the item numbers of an invoice by ID.
     * @param conn The connection to the database.
     * @param invoiceId The ID of the invoice.
     * @return The IDs of the invoice items.
     */
    public static LinkedList<Integer> getItems(Connection conn, Integer invoiceId) {
        try {
            String query = "SELECT item_num FROM invoice_items WHERE invoice_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();
            LinkedList<Integer> ids = new LinkedList<>();
            while (rs.next()) ids.add(rs.getInt("item_num"));
            pstmt.close();
            rs.close();
            return ids;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if an invoice item exists with the given ID and number.
     * @param conn The connection to the database.
     * @param invoiceId The ID of the invoice.
     * @param itemNum The invoice item number.
     * @return True if the invoice item exists, false otherwise.
     */
    public static boolean exists(Connection conn, Integer invoiceId, Integer itemNum) {
        try {
            String query = """
                SELECT *
                    FROM invoice_items
                    WHERE invoice_id = ?
                        AND item_num = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, invoiceId);
            pstmt.setInt(2, itemNum);
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

