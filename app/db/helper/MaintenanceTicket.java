import java.sql.*;

/**
 * Helper class to handle the maintenance tickets table in the database.
 */
public class MaintenanceTicket {
    
    /**
     * Insert a maintenance ticket into the maintenance tickets table.
     * Automatically records the current date as the report date.
     * @param conn The connection to the database.
     * @param equipmentId The ID of the equipment.
     * @param description The description of the repairs needed.
     * @return True if successfully added, false otherwise.
     */
    public static boolean add(
        Connection conn,
        Integer equipmentId,
        String description
    ) {
        try {
            String query = """
                INSERT INTO maintenance_tickets (
                    equipmentId,
                    reportDate,
                    description,
                    being_repaired,
                    is_resolved,
                    resolved_date
                ) VALUES (?, ?, ?, ?, ?, ?)
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, equipmentId);
            pstmt.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pstmt.setString(3, description);
            pstmt.setBoolean(4, false);
            pstmt.setBoolean(5, false);
            pstmt.setDate(6, null);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Update the repairing status of a piece of equipment.
     * @param conn The connection to the database.
     * @param ticketId The ID of the maintenance ticket.
     * @param beingRepaired The repairing status to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateRepairing(Connection conn, Integer ticketId, boolean beingRepaired) {
        try {
            String query = """
                UPDATE fitness_goals
                    SET being_repaired = ?
                    WHERE ticket_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setBoolean(1, beingRepaired);
            pstmt.setInt(2, ticketId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Mark a maintenance ticket as resolved.
     * Automatically records the current date as the resolution date.
     * Automatically updates repairing status to false.
     * @param conn The connection to the database.
     * @param ticketId The ID of the maintenance ticket.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean markResolved(Connection conn, Integer ticketId) {
        try {
            String query = """
                UPDATE maintenance_tickets
                    SET is_resolved = ?
                    WHERE ticket_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setBoolean(1, true);
            pstmt.setInt(2, ticketId);
            pstmt.executeUpdate();
            pstmt.close();
            query = """
                UPDATE maintenance_tickets
                    SET resolved_date = ?
                    WHERE ticket_id = ?
                """;
            pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pstmt.setInt(2, ticketId);
            pstmt.executeUpdate();
            pstmt.close();
            updateRepairing(conn, ticketId, false);
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }
}
