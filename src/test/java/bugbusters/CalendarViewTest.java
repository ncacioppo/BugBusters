package bugbusters;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CalendarViewTest {

    @Test
    void printScheduleAsCalendar() {
        //no assertEquals(); just printing to console from here
        Schedule schedule = ScheduleTest.getSampleSchedule();
        ArrayList<Course> springCourses = schedule.getCourses();

        //sanity check: print each course TODO:delete after testing
        for(Course c : springCourses) {
            System.out.println(c.toString());
        }

        //print schedule in calendar format
//        CalendarView calendarView = new CalendarView(schedule);
//        calendarView.printScheduleAsCalendar();
//        schedule.printScheduleAsCalendar();
    }

    public static void main(String[] args) {
        Schedule schedule = ScheduleTest.getSampleSchedule();
        System.out.println(schedule.getName());
        CalendarView calendarView = new CalendarView(schedule);
        calendarView.printScheduleAsCalendar();
    }
}