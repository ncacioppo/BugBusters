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

    public static ArrayList<Course> getTestCourses() {
        ArrayList<Course> springCourses = new ArrayList<Course>();

        //ACCT 201
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
                ACCT201A_meetingTimes, 4));

        return springCourses;
    }

    @Test
    void printScheduleAsCalendar() {
        ArrayList<Course> springCourses = getTestCourses();
        Schedule schedule = new Schedule("Spring 2020 #1",new Term("Spring",2020),
                springCourses);
    }
}