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
        ArrayList<Course> springCourses = getTestCourses();

        //sanity check: print each course TODO:delete after testing
        for(Course c : springCourses) {
            System.out.println(c.toString());
        }

        //create new schedule
        Schedule schedule = new Schedule("Spring 2020 #1",new Term("Spring",2020),
                springCourses);

        //print schedule in calendar format
        CalendarView calendarView = new CalendarView(schedule);
        calendarView.printScheduleAsCalendar();
//        schedule.printScheduleAsCalendar();
    }

    public static ArrayList<Course> getTestCourses() {
        ArrayList<Course> springCourses = new ArrayList<Course>();

        //ACCT 201 A
        Set<MeetingTime> ACCT201A_meetingTimes = new HashSet<>();
        ACCT201A_meetingTimes.add(
                new MeetingTime(Day.MONDAY, LocalTime.of(9,0,0),
                        LocalTime.of(9,50,0)));
        ACCT201A_meetingTimes.add(
                new MeetingTime(Day.WEDNESDAY, LocalTime.of(9,0,0),
                        LocalTime.of(9,50,0)));
        ACCT201A_meetingTimes.add(
                new MeetingTime(Day.FRIDAY, LocalTime.of(9,0,0),
                        LocalTime.of(9,50,0)));

        springCourses.add(new Course(289410, "PRINCIPLES OF ACCOUNTING I", "", "ACCT",
                201, new Term("Spring", 2020), 'A', "Stone, Jennifer",
                ACCT201A_meetingTimes, 3));

        //COMP 244 B
        Set<MeetingTime> COMP244B_meetingTimes = new HashSet<>();
        COMP244B_meetingTimes.add(
                new MeetingTime(Day.TUESDAY, LocalTime.of(11,30,0),
                        LocalTime.of(12,45,0)));
        COMP244B_meetingTimes.add(
                new MeetingTime(Day.THURSDAY, LocalTime.of(11,30,0),
                        LocalTime.of(12,45,0)));

        springCourses.add(new Course(927348, "DATABASE MGT SYSTEMS", "", "COMP",
                244, new Term("Spring", 2020), 'B', "Al Moakar, Lory",
                COMP244B_meetingTimes, 3));

        return springCourses;
    }
}