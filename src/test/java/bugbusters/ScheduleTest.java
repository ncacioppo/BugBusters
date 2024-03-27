package bugbusters;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {
    Term fall23 = new Term("Fall", 2023);
    Term spring24 = new Term("Spring", 2024);
    @Test
    void addCourse() {
        Set<MeetingTime> meetingTimes = new HashSet<>();
        meetingTimes.add(new MeetingTime(Day.MONDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.THURSDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));

        Set<MeetingTime> meetingTimes2 = new HashSet<>();
        meetingTimes2.add(new MeetingTime(Day.MONDAY, LocalTime.of(1, 00), LocalTime.of(1, 50)));
        meetingTimes2.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(1, 00), LocalTime.of(1, 50)));
        meetingTimes2.add(new MeetingTime(Day.FRIDAY, LocalTime.of(1, 00), LocalTime.of(1, 50)));

        Course course1 = new Course(111111, "Programming 1", "learn to code good", "COMP", 141, fall23, 'A', "Dr. Hutchins", meetingTimes, 3);
        Course course2 = new Course(222222, "Intro to Comp Sci", "description", "COMP", 155, fall23, 'A', "Dr. Dickinson", meetingTimes, 3);
        Course course3 = new Course(111112, "Programming 1", "learn to code good", "COMP", 141, fall23, 'B', "Dr. Hutchins", meetingTimes2, 3);

        ArrayList<Course> courses = new ArrayList<>();

        Schedule mySchedule = new Schedule("My Schedule", fall23, courses);

        assertTrue(mySchedule.addCourse(course1));
        mySchedule.addCourse(course1);
        assertFalse(mySchedule.addCourse(course2));
        assertFalse(mySchedule.addCourse(course3));

    }

    @Test
    void removeCourse() {
        Set<MeetingTime> meetingTimes = new HashSet<>();
        meetingTimes.add(new MeetingTime(Day.MONDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.THURSDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));

        Set<MeetingTime> meetingTimes2 = new HashSet<>();
        meetingTimes2.add(new MeetingTime(Day.MONDAY, LocalTime.of(1, 00), LocalTime.of(1, 50)));
        meetingTimes2.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(1, 00), LocalTime.of(1, 50)));
        meetingTimes2.add(new MeetingTime(Day.FRIDAY, LocalTime.of(1, 00), LocalTime.of(1, 50)));

        Course course1 = new Course(111111, "Programming 1", "learn to code good", "COMP", 141, fall23, 'A', "Dr. Hutchins", meetingTimes, 3);
        Course course2 = new Course(222222, "Intro to Comp Sci", "description", "COMP", 155, fall23, 'A', "Dr. Dickinson", meetingTimes, 3);
        Course course3 = new Course(111112, "Programming 1", "learn to code good", "COMP", 141, fall23, 'B', "Dr. Hutchins", meetingTimes2, 3);

        ArrayList<Course> courses = new ArrayList<>();

        Schedule mySchedule = new Schedule("My Schedule", fall23, courses);
        mySchedule.addCourse(course1);
        assertEquals(course1, mySchedule.removeCourse(course1));
        assertNull(mySchedule.removeCourse(course2));
    }

    @Test
    void testRemoveCourse() {
        Set<MeetingTime> meetingTimes = new HashSet<>();
        meetingTimes.add(new MeetingTime(Day.MONDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.THURSDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));

        Set<MeetingTime> meetingTimes2 = new HashSet<>();
        meetingTimes2.add(new MeetingTime(Day.MONDAY, LocalTime.of(1, 00), LocalTime.of(1, 50)));
        meetingTimes2.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(1, 00), LocalTime.of(1, 50)));
        meetingTimes2.add(new MeetingTime(Day.FRIDAY, LocalTime.of(1, 00), LocalTime.of(1, 50)));

        Course course1 = new Course(111111, "Programming 1", "learn to code good", "COMP", 141, fall23, 'A', "Dr. Hutchins", meetingTimes, 3);
        Course course2 = new Course(222222, "Intro to Comp Sci", "description", "COMP", 155, fall23, 'A', "Dr. Dickinson", meetingTimes, 3);
        Course course3 = new Course(111112, "Programming 1", "learn to code good", "COMP", 141, fall23, 'B', "Dr. Hutchins", meetingTimes2, 3);

        ArrayList<Course> courses = new ArrayList<>();

        Schedule mySchedule = new Schedule("My Schedule", fall23, courses);
        mySchedule.addCourse(course1);
        assertEquals(course1, mySchedule.removeCourse(111111));
        assertNull(mySchedule.removeCourse(141));
    }

    @Test
    void testRemoveCourse1() {
        Set<MeetingTime> meetingTimes = new HashSet<>();
        meetingTimes.add(new MeetingTime(Day.MONDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.THURSDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));

        Set<MeetingTime> meetingTimes2 = new HashSet<>();
        meetingTimes2.add(new MeetingTime(Day.MONDAY, LocalTime.of(1, 00), LocalTime.of(1, 50)));
        meetingTimes2.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(1, 00), LocalTime.of(1, 50)));
        meetingTimes2.add(new MeetingTime(Day.FRIDAY, LocalTime.of(1, 00), LocalTime.of(1, 50)));

        Course course1 = new Course(111111, "Programming 1", "learn to code good", "COMP", 141, fall23, 'A', "Dr. Hutchins", meetingTimes, 3);
        Course course2 = new Course(222222, "Intro to Comp Sci", "description", "COMP", 155, fall23, 'A', "Dr. Dickinson", meetingTimes, 3);
        Course course3 = new Course(111112, "Programming 1", "learn to code good", "COMP", 141, fall23, 'B', "Dr. Hutchins", meetingTimes2, 3);

        ArrayList<Course> courses = new ArrayList<>();

        Schedule mySchedule = new Schedule("My Schedule", fall23, courses);
        mySchedule.addCourse(course1);
        assertEquals(course1, mySchedule.removeCourse("Programming 1"));
        assertNull(mySchedule.removeCourse("Intro to Data Structures and Algorithms"));
    }
}