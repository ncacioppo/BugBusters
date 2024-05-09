package bugbusters.UI;

import bugbusters.*;
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

    public static DatabaseSearch dbSearch;

    public static boolean sync = false;
    public static String currentKeyword = "";
    public static String currentTerm = "";
    public static String currentProfessor = "";
    public static String currentDept = "";
    public static int currentMinCode = 0;
    public static int currentMaxCode = 699;
    public static boolean currentMWF = false;
    public static boolean currentTR = false;

    private Globals(){}
}
