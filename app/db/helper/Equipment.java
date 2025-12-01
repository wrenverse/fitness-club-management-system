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
     * Update the room of a piece of equipment.
     * @param conn The connection to the database.
     * @param equipmentId The ID of the piece of equipment.
     * @param roomId The ID of the room to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateRoom(Connection conn, Integer equipmentId, Integer roomId) {
        try {
            if (!(exists(conn, equipmentId))) return false;
            String query = "UPDATE equipment SET room_id = ? WHERE equipment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, roomId);
            pstmt.setInt(2, equipmentId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Update the name of a piece of equipment.
     * @param conn The connection to the database.
     * @param equipmentId The ID of the piece of equipment.
     * @param name The name to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateName(Connection conn, Integer equipmentId, String name) {
        try {
            if (!(exists(conn, equipmentId))) return false;
            String query = "UPDATE equipment SET name = ? WHERE equipment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setInt(2, equipmentId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
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
            if (!exists(conn, equipmentId)) return false;
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

    /**
     * Get the room ID of a piece of equipment by ID.
     * @param conn The connection to the database.
     * @param equipmentId The ID of the piece of equipment.
     * @return The room ID of the piece of equipment.
     */
    public static Integer getRoomId(Connection conn, Integer equipmentId) {
        try {
            if (!exists(conn, equipmentId)) return null;
            String query = "SELECT room_id FROM equipment WHERE equipment = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, equipmentId);
            Integer id = pstmt.executeQuery(query).getInt("room_id");
            pstmt.close();
            return id;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the name of a piece of equipment by ID.
     * @param conn The connection to the database.
     * @param equipmentId The ID of the piece of equipment.
     * @return The name of the piece of equipment.
     */
    public static String getName(Connection conn, Integer equipmentId) {
        try {
            if (!exists(conn, equipmentId)) return null;
            String query = "SELECT name FROM equipment WHERE equipment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, equipmentId);
            String name = pstmt.executeQuery(query).getString("name");
            pstmt.close();
            return name;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if a piece of equipment with the given ID is operational.
     * @param conn The connection to the database.
     * @param equipmentId The ID of the piece of equipment.
     * @return True if the piece of equipment is operational, false otherwise.
     */
    public static boolean isOperational(Connection conn, Integer equipmentId) {
        try {
            if (!exists(conn, equipmentId)) return false;
            String query = "SELECT is_operational FROM equipment WHERE equipment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, equipmentId);
            boolean isOperational = pstmt.executeQuery(query).getBoolean("is_operational");
            pstmt.close();
            return isOperational;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }

    /**
     * Check if a piece of equipment exists with the given ID.
     * @param conn The connection to the database.
     * @param equipmentId The ID of the piece of equipment.
     * @return True if the piece of equipment exists, false otherwise.
     */
    public static boolean exists(Connection conn, Integer equipmentId) {
        try {
            String query = "SELECT equipment_id FROM equipment WHERE equipment_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, equipmentId);
            boolean exists = pstmt.executeQuery(query).next();
            pstmt.close();
            return exists;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }
}
