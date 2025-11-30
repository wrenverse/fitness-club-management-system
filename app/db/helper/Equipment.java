import java.sql.*;

/**
 * Helper class to handle the equipment table in the database.
 */
public class Equipment {
    
    /**
     * Insert a piece of equipment into the equipment table.
     * @param conn The connection to the database.
     * @param roomId The ID of the room.
     * @param name The name of the equipment.
     * @param isOperational The operational status of the equipment.
     * @return True if successfully added, false otherwise.
     */
    public static boolean add(
        Connection conn,
        Integer roomId,
        String name,
        boolean isOperational
    ) {
        try {
            String query = """
                INSERT INTO equipment (
                    room_id,
                    name,
                    is_operational
                ) VALUES (?, ?, ?)
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, roomId);
            pstmt.setString(2, name);
            pstmt.setBoolean(3, isOperational);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Get the information of a piece of equipment by ID.
     * @param conn The connection to the database.
     * @param equipmentId The ID of the equipment.
     * @return The table row containing the information.
     */
    public static ResultSet getInfo(Connection conn, Integer equipmentId) {
        try {
            String query = "SELECT * FROM equipment WHERE equipment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, equipmentId);
            return pstmt.executeQuery(query);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Update the operational status of a piece of equipment.
     * @param conn The connection to the database.
     * @param equipmentId The ID of the equipment.
     * @param isOperational The operational status to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateOperational(
        Connection conn,
        Integer equipmentId,
        boolean isOperational
    ) {
        try {
            String query = """
                UPDATE equipment
                    SET is_operational = ?
                    WHERE equipment_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setBoolean(1, isOperational);
            pstmt.setInt(2, equipmentId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }
}
