import java.sql.Timestamp;

/**
 * Utilities class for handling miscellaneous tasks.
 */
public class Utilities {
    
    public static boolean overlaps(
        Timestamp s1,
        Timestamp e1,
        Timestamp s2,
        Timestamp e2
    ) {
        return s1.before(e2) && s2.before(e1);
    }
}
