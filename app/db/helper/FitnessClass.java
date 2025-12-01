import java.sql.*;

/**
 * Helper class to handle the classes table in the database.
 */
public class FitnessClass {
    
    /**
     * Insert a class into the classes table.
     * @param conn The connection to the database.
     * @param trainerId The ID of the trainer.
     * @param roomId The ID of the room.
     * @param capacity The capacity of the class.
     * @param startTimestamp The starting timestamp of the session.
     * @param endTimestamp The end timestamp of the session.
     * @return True if successfully added, false otherwise.
     */
    public static boolean add(
        Connection conn,
        Integer trainerId,
        Integer roomId,
        Integer capacity,
        Timestamp startTimestamp,
        Timestamp endTimestamp
    ) {
        try {
            // Prevent a trainer from registering for creating a conflicting class.
            if (isConflicting(conn, trainerId, roomId, startTimestamp, endTimestamp))
                return false;
            String query = """
                INSERT INTO classes (
                    trainer_id,
                    room_id,
                    capacity
                    start_timestamp,
                    end_timestamp
                ) VALUES (?, ?, ?, ?, ?)
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            pstmt.setInt(2, roomId);
            pstmt.setInt(3, capacity);
            pstmt.setTimestamp(4, startTimestamp);
            pstmt.setTimestamp(5, endTimestamp);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Delete a class by ID.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @return True if successfully deleted, false otherwise.
     */
    public static boolean delete(Connection conn, Integer classId) {
        try {
            if (!(exists(conn, classId))) return false;
            String query = "DELETE FROM classes WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, classId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Update the trainer of a class.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @param trainerId The trainer to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateTrainer(Connection conn, Integer classId, Integer trainerId) {
        try {
            if (!(exists(conn, classId))) return false;
            String query = "UPDATE classes SET trainer_id = ? WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, trainerId);
            pstmt.setInt(2, classId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Update the room of a class.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @param roomId The ID of the room to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateRoom(Connection conn, Integer classId, Integer roomId) {
        try {
            if (!(exists(conn, classId))) return false;
            String query = "UPDATE classes SET room_id = ? WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, roomId);
            pstmt.setInt(2, classId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Update the name of a class.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @param name The name to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateName(Connection conn, Integer classId, String name) {
        try {
            if (!(exists(conn, classId))) return false;
            String query = "UPDATE classes SET name = ? WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setInt(2, classId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Update the starting timestamp of a class.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @param ts The timestamp to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateStart(Connection conn, Integer classId, Timestamp ts) {
        try {
            if (!(exists(conn, classId))) return false;
            String query = "UPDATE classes SET start_timestamp = ? WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setTimestamp(1, ts);
            pstmt.setInt(2, classId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Update the ending timestamp of a member.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @param ts The timestamp to be modified to.
     * @return True if successfully modified, false otherwise.
     */
    public static boolean updateEnd(Connection conn, Integer classId, Timestamp ts) {
        try {
            if (!(exists(conn, classId))) return false;
            String query = "UPDATE classes SET end_timestamp = ? WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setTimestamp(1, ts);
            pstmt.setInt(2, classId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            Terminal.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Get the name of a class by ID.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @return The name of the class.
     */
    public static String getName(Connection conn, Integer classId) {
        try {
            if (!exists(conn, classId)) return null;
            String query = "SELECT name FROM classes WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, classId);
            String name = pstmt.executeQuery(query).getString("name");
            pstmt.close();
            return name;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the capacity of a class.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @return The capacity of the class.
     */
    public static Integer getCapacity(Connection conn, Integer classId) {
        try {
            if (!(exists(conn, classId))) return null;
            String query = "SELECT capacity FROM classes WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, classId);
            return pstmt.executeQuery(query).getInt("capacity");
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the starting timestamp of a class.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @return The starting timestamp of the class.
     */
    public static Timestamp getStart(Connection conn, Integer classId) {
        try {
            if (!(exists(conn, classId))) return null;
            String query = "SELECT start_timestamp FROM classes WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, classId);
            return pstmt.executeQuery(query).getTimestamp("start_timestamp");
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Get the ending timestamp of a class.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @return The ending timestamp of the class.
     */
    public static Timestamp getEnd(Connection conn, Integer classId) {
        try {
            if (!(exists(conn, classId))) return null;
            String query = "SELECT end_timestamp FROM classes WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, classId);
            return pstmt.executeQuery(query).getTimestamp("end_timestamp");
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    /**
     * Check if a time slot conflicts with existing time slots.
     * @param conn The connection to the database.
     * @param trainerId The ID of the trainer with the schedule to be checked.
     * @param roomId The room ID of the room to be checked.
     * @param startTimestamp The starting timestamp of the time slot.
     * @param endTimestamp The end timestamp of the time slot.
     * @return True if there exists a conflict or failed to find a match for the ID,
     *  false otherwise.
     */
    public static boolean isConflicting(
        Connection conn,
        Integer memberId,
        Integer roomId,
        Timestamp startTimestamp,
        Timestamp endTimestamp
    ) {
        try {
            // Get all the classes of the trainer.
            String query = """
                SELECT start_timestamp, end_timestamp
                    FROM classes
                    WHERE trainer_id = ?
                        OR room_id = ?
                """;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, roomId);
            ResultSet rs = pstmt.executeQuery(query);

            // Check each class for scheduling conflicts.
            while(rs.next()) {
                Timestamp st = rs.getTimestamp("start_timestamp");
                Timestamp et = rs.getTimestamp("end_timestamp");
                if (Utilities.overlaps(startTimestamp, endTimestamp, st, et))
                    return true;
            }

        } catch (Exception e) {
            Terminal.exception(e);
            return true;
        }
        return false;
    }

    /**
     * Check if a class exists with the given ID.
     * @param conn The connection to the database.
     * @param classId The ID of the class.
     * @return True if the class exists, false otherwise.
     */
    public static boolean exists(Connection conn, Integer classId) {
        try {
            String query = "SELECT class_id FROM classes WHERE class_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, classId);
            boolean exists = pstmt.executeQuery(query).next();
            pstmt.close();
            return exists;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }
}
