package bugbusters.UI;

import bugbusters.Course;
import bugbusters.Schedule;
import bugbusters.User;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class Globals {
    public static  User actualUser = null;
    public static Map<String, Schedule> userSchedules = new HashMap<>();
    public static Schedule currentSchedule = null;
    public static Course currentCourse = null;
    public static ArrayList<Course> searchCourses = new ArrayList<>();
    public static Set<String> departments = new HashSet<>();
    public static Set<String> seasons = new HashSet<>();
    public static Set<Integer> years = new HashSet<>();

    private Globals(){}
}
