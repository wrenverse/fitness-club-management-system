import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.LinkedList;

/**
 * Controller to keep DB calls out of the CLI.
 */
public class Controller {

    private final Connection conn;

    public Controller(Connection conn) {
        this.conn = conn;
    }

    public boolean registerMember(
        String name,
        Date dob,
        String gender,
        String email,
        String phone
    ) {
        if (Member.emailExists(conn, email)) {
            Terminal.error("A member with that email already exists.");
            return false;
        }
        return Member.add(conn, name, dob, gender, email, phone);
    }

    public Integer getMemberIdByEmail(String email) {
        try {
            String query = "SELECT member_id FROM members WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            Integer id = null;
            if (rs.next()) id = rs.getInt("member_id");
            rs.close();
            pstmt.close();
            return id;
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return null;
    }

    public String getMemberName(Integer memberId) {
        return Member.getName(conn, memberId);
    }

    public Date getMemberDOB(Integer memberId) {
        return Member.getDOB(conn, memberId);
    }

    public String getMemberGender(Integer memberId) {
        return Member.getGender(conn, memberId);
    }

    public String getMemberEmail(Integer memberId) {
        return Member.getEmail(conn, memberId);
    }

    public String getMemberPhone(Integer memberId) {
        return Member.getPhone(conn, memberId);
    }

    public boolean updateMemberName(Integer memberId, String name) {
        return Member.updateName(conn, memberId, name);
    }

    public boolean updateMemberDOB(Integer memberId, Date dob) {
        return Member.updateDOB(conn, memberId, dob);
    }

    public boolean updateMemberGender(Integer memberId, String gender) {
        return Member.updateGender(conn, memberId, gender);
    }

    public boolean updateMemberEmail(Integer memberId, String email) {
        try {
            Integer existing = getMemberIdByEmail(email);
            if (existing != null && !existing.equals(memberId)) {
                Terminal.error("Another member already uses that email.");
                return false;
            }
            return Member.updateEmail(conn, memberId, email);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }

    public boolean updateMemberPhone(Integer memberId, String phone) {
        return Member.updatePhone(conn, memberId, phone);
    }

    public boolean addFitnessGoal(
        Integer memberId,
        Integer typeId,
        Float targetValue,
        Date targetDate,
        Date startDate
    ) {
        try {
            if (!GoalType.exists(conn, typeId)) return false;
            return FitnessGoal.add(conn, memberId, typeId, targetValue, targetDate, startDate);
        } catch (Exception e) {
            Terminal.exception(e);
        }
        return false;
    }

    public LinkedList<Integer> getFitnessGoals(Integer memberId) {
        return FitnessGoal.getGoals(conn, memberId);
    }

    public Integer getGoalTypeId(Integer goalId) {
        return FitnessGoal.getTypeId(conn, goalId);
    }

    public String getGoalTypeName(Integer goalId) {
        Integer typeId = getGoalTypeId(goalId);
        if (typeId == null) return null;
        return GoalType.getName(conn, typeId);
    }

    public String getGoalTypeUnit(Integer goalId) {
        Integer typeId = getGoalTypeId(goalId);
        if (typeId == null) return null;
        return GoalType.getUnit(conn, typeId);
    }

    public Float getGoalTargetValue(Integer goalId) {
        return FitnessGoal.getTargetValue(conn, goalId);
    }

    public Date getGoalTargetDate(Integer goalId) {
        return FitnessGoal.getTargetDate(conn, goalId);
    }

    public Date getGoalStartDate(Integer goalId) {
        return FitnessGoal.getStartDate(conn, goalId);
    }

    public boolean isGoalCompleted(Integer goalId) {
        return FitnessGoal.isCompleted(conn, goalId);
    }

    public boolean addHealthMetric(
        Integer memberId,
        Integer heartRate,
        Float bodyFat,
        Integer weight,
        Integer height
    ) {
        return HealthMetric.add(conn, memberId, heartRate, bodyFat, weight, height);
    }

    public LinkedList<Integer> getHealthMetrics(Integer memberId) {
        return HealthMetric.getRecords(conn, memberId);
    }

    public Timestamp getHealthMetricTimestamp(Integer metricId) {
        return HealthMetric.getTimestamp(conn, metricId);
    }

    public Integer getHealthMetricHeartRate(Integer metricId) {
        return HealthMetric.getHeartRate(conn, metricId);
    }

    public Float getHealthMetricBodyFat(Integer metricId) {
        return HealthMetric.getBodyFat(conn, metricId);
    }

    public Integer getHealthMetricWeight(Integer metricId) {
        return HealthMetric.getWeight(conn, metricId);
    }

    public Integer getHealthMetricHeight(Integer metricId) {
        return HealthMetric.getHeight(conn, metricId);
    }
}
