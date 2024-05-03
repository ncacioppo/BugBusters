package bugbusters.UI;

import bugbusters.Course;
import bugbusters.Schedule;
import bugbusters.Term;
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
    public static Set<String> terms = new HashSet<>();
    public static Set<String> professors = new HashSet<>();
    public static String prevDept = "";
    public static String prevTerm = "";
    public static String prevProf = "";
    public static String prevMin = "";
    public static String prevMax = "";
    public static String prevKeyword = "";

    private Globals(){}
}
