import java.sql.*;
import java.util.LinkedList;

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
            if (!exists(conn, ticketId)) return false;
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
            if (!exists(conn, ticketId)) return false;
            String query = """
                UPDATE maintenance_tickets
                    SET is_repaired = ?
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

    /**
     * Get the equipment ID of a maintenance ticket by ID.
     * @param conn The connection to the database.
     * @param ticketId The ID of the ticket.
     * @return The equipment ID of the ticket.
     */
    public static Integer getEquipmentId(Connection conn, Integer ticketId) {
        try {
            if (!exists(conn, ticketId)) return null;
            String query = "SELECT equipment_id FROM maintenance_tickets WHERE ticket_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, ticketId);
            Integer id = pstmt.executeQuery(query).getInt("equipment_id");
            pstmt.close();
            return id;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the report date of a maintenance ticket by ID.
     * @param conn The connection to the database.
     * @param ticketId The ID of the ticket.
     * @return The report date of the ticket.
     */
    public static Date getReportDate(Connection conn, Integer ticketId) {
        try {
            if (!exists(conn, ticketId)) return null;
            String query = "SELECT report_date FROM maintenance_tickets WHERE ticket_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, ticketId);
            Date date = pstmt.executeQuery(query).getDate("report_date");
            pstmt.close();
            return date;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the description of a maintenance ticket by ID.
     * @param conn The connection to the database.
     * @param ticketId The ID of the ticket.
     * @return The description of the maintenance ticket.
     */
    public static String getDescription(Connection conn, Integer ticketId) {
        try {
            if (!exists(conn, ticketId)) return null;
            String query = "SELECT description FROM maintenance_tickets WHERE ticket_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, ticketId);
            String gender = pstmt.executeQuery(query).getString("description");
            pstmt.close();
            return gender;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if a ticket problem with the given ID is being repaired.
     * @param conn The connection to the database.
     * @param ticketId The ID of ticket.
     * @return True if the ticket is bring repaired, false otherwise.
     */
    public static boolean isBeingRepaired(Connection conn, Integer ticketId) {
        try {
            if (!exists(conn, ticketId)) return false;
            String query = "SELECT being_repaired FROM maintenance_tickets WHERE ticket_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, ticketId);
            boolean isOperational = pstmt.executeQuery(query).getBoolean("being_repaired");
            pstmt.close();
            return isOperational;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }

    /**
     * Check if a ticket problem with the given ID is repaired.
     * @param conn The connection to the database.
     * @param ticketId The ID of ticket.
     * @return True if the ticket is repaired, false otherwise.
     */
    public static boolean isRepaired(Connection conn, Integer ticketId) {
        try {
            if (!exists(conn, ticketId)) return false;
            String query = "SELECT is_repaired FROM maintenance_tickets WHERE ticket_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, ticketId);
            boolean isOperational = pstmt.executeQuery(query).getBoolean("is_repaired");
            pstmt.close();
            return isOperational;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }

    /**
     * Get the resolution date of a maintenance ticket by ID.
     * @param conn The connection to the database.
     * @param ticketId The ID of the ticket.
     * @return The resolution date of the ticket.
     */
    public static Date getResolutionDate(Connection conn, Integer ticketId) {
        try {
            if (!exists(conn, ticketId)) return null;
            if (!isRepaired(conn, ticketId)) return null;
            String query = "SELECT resolved_date FROM maintenance_tickets WHERE ticket_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, ticketId);
            Date date = pstmt.executeQuery(query).getDate("resolved_date");
            pstmt.close();
            return date;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the IDs of all maintenance tickets.
     * @param conn The connection to the database.
     * @return The IDs of the tickets.
     */
    public static LinkedList<Integer> getAllTickets(Connection conn) {
        try {
            String query = "SELECT ticket_id FROM maintenance_tickets";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery(query);
            LinkedList<Integer> ids = new LinkedList<>();
            while (rs.next()) ids.add(rs.getInt("ticket_id"));
            pstmt.close();
            rs.close();
            return ids;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if a maintenance ticket exists with the given ID.
     * @param conn The connection to the database.
     * @param ticketId The ID of the maintenance ticket.
     * @return True if the ticket exists, false otherwise.
     */
    public static boolean exists(Connection conn, Integer ticketId) {
        try {
            String query = "SELECT ticket_id FROM maintenance_tickets WHERE ticket_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, ticketId);
            boolean exists = pstmt.executeQuery(query).next();
            pstmt.close();
            return exists;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }
}
