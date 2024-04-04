package bugbusters;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    @Test
    void getName() {
        Schedule schedule = getSampleSchedule();
        assertEquals("Spring 2020 #1",schedule.getName());
    }

    @Test
    void getTerm() {
    }

    @Test
    void getCourses() {
    }

    @Test
    void addCourse() {
    }

    @Test
    void removeCourse() {
    }

    @Test
    void testRemoveCourse() {
    }

    @Test
    void testRemoveCourse1() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testToString() {
    }

    public static Schedule getSampleSchedule() {
        ArrayList<Course> springCourses = new ArrayList<>();

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

        //HUMA 301 F
        Set<MeetingTime> HUMA301F_meetingTimes = new HashSet<>();
        HUMA301F_meetingTimes.add(
                new MeetingTime(Day.TUESDAY, LocalTime.of(10,0,0),
                        LocalTime.of(11,20,0)));
        HUMA301F_meetingTimes.add(
                new MeetingTime(Day.THURSDAY, LocalTime.of(10,0,0),
                        LocalTime.of(11,20,0)));

        springCourses.add(new Course(100653, "CIV/THE ARTS", "", "HUMA",
                301, new Term("Spring", 2020), 'F', "Munson, Paul",
                HUMA301F_meetingTimes, 3));

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


        //sanity check: print each course TODO:delete after testing
        for(Course c : springCourses) {
            System.out.println(c.toString());
        }

        //create new schedule
        Schedule schedule = new Schedule("Spring 2020 #1",new Term("Spring",2020),
                springCourses);

        return schedule;
    }
}