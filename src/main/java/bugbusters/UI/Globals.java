package bugbusters.UI;

import bugbusters.Course;
import bugbusters.Schedule;
import bugbusters.User;

import java.util.HashMap;
import java.util.Map;

public class Globals {
    public static  User actualUser = null;
    public static Map<String, Schedule> userSchedules = new HashMap<>();
    public static Schedule currentSchedule = null;
    public static Course currentCourse = null;

    private Globals(){}
}
