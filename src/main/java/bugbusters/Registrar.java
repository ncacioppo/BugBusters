package bugbusters;

import java.util.List;
import java.util.Set;

public class Registrar {
    private static Set<String> majors;  //may want to use list indexing is more efficient
    private static Set<String> minors;
    private static List<Course> courses;

    private boolean connectToDB() {
        return false;
    }

    /**
     * Calls connectToDB() and pulls major titles into a set
     * B.S. in Computer Science, B.A. in Computer Science, or B.S. in Data Science
     * combines ex. "B.S." + " in " + "Computer Science"
     * @return set of major names
     */
    public static Set<String> getMajors() {
        return majors;
    }

    public static Set<String> getMinors() {
        return minors;
    }

    public static List<Course> getCourses() {
        return courses;
    }
}
