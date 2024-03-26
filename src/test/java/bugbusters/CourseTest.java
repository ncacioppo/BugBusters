package bugbusters;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class  CourseTest {

    @Test
    void getId() {
        Set<MeetingTime> meetingTimes = new HashSet<>();
        meetingTimes.add(new MeetingTime(Day.MONDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.THURSDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        Course course = new Course(0, "name", "description", "department", 10, new Term("Fall", 2018),
                'A', "instructor", meetingTimes, 3);

        assertEquals(0, course.getId());
    }

    @Test
    void getName() {
        Set<MeetingTime> meetingTimes = new HashSet<>();
        meetingTimes.add(new MeetingTime(Day.MONDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.THURSDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        Course course = new Course(0, "name", "description", "department", 10, new Term("Fall", 2018),
                'A', "instructor", meetingTimes, 3);

        assertEquals("name", course.getName());
    }

    @Test
    void getDescription() {
        Set<MeetingTime> meetingTimes = new HashSet<>();
        meetingTimes.add(new MeetingTime(Day.MONDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.THURSDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        Course course = new Course(0, "name", "description", "department", 10, new Term("Fall", 2018),
                'A', "instructor", meetingTimes, 3);

        assertEquals("description", course.getDescription());
    }

    @Test
    void getDepartment() {
        Set<MeetingTime> meetingTimes = new HashSet<>();
        meetingTimes.add(new MeetingTime(Day.MONDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.THURSDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        Course course = new Course(0, "name", "description", "department", 10, new Term("Fall", 2018),
                'A', "instructor", meetingTimes, 3);

        assertEquals("department", course.getDepartment());
    }

    @Test
    void getCode() {
        Set<MeetingTime> meetingTimes = new HashSet<>();
        meetingTimes.add(new MeetingTime(Day.MONDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.THURSDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        Course course = new Course(0, "name", "description", "department", 10, new Term("Fall", 2018),
                'A', "instructor", meetingTimes, 3);

        assertEquals(10, course.getCode());
    }

    @Test
    void getTerm() {
        Set<MeetingTime> meetingTimes = new HashSet<>();
        meetingTimes.add(new MeetingTime(Day.MONDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.THURSDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        Course course = new Course(0, "name", "description", "department", 10, new Term("Fall", 2018),
                'A', "instructor", meetingTimes, 3);


        assertEquals("Fall", course.getTerm().getSeason());
        assertEquals(2018, course.getTerm().getYear());
    }

    @Test
    void getSection() {
        Set<MeetingTime> meetingTimes = new HashSet<>();
        meetingTimes.add(new MeetingTime(Day.MONDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.THURSDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        Course course = new Course(0, "name", "description", "department", 10, new Term("Fall", 2018),
                'A', "instructor", meetingTimes, 3);

        assertEquals('A', course.getSection());
    }

    @Test
    void getInstructor() {
        Set<MeetingTime> meetingTimes = new HashSet<>();
        meetingTimes.add(new MeetingTime(Day.MONDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.THURSDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        Course course = new Course(0, "name", "description", "department", 10, new Term("Fall", 2018),
                'A', "instructor", meetingTimes, 3);

        assertEquals("instructor", course.getInstructor());
    }



    @Test
    void getMeetingTimes() {
        Set<MeetingTime> meetingTimes = new HashSet<>();
        meetingTimes.add(new MeetingTime(Day.MONDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.THURSDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        Course course = new Course(0, "name", "description", "department", 10, new Term("Fall", 2018),
                'A', "instructor", meetingTimes, 3);

        assertEquals(meetingTimes, course.getMeetingTimes());
    }

    @Test
    void getCredits() {
        Set<MeetingTime> meetingTimes = new HashSet<>();
        meetingTimes.add(new MeetingTime(Day.MONDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.WEDNESDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        meetingTimes.add(new MeetingTime(Day.THURSDAY, LocalTime.of(10, 00), LocalTime.of(10, 50)));
        Course course = new Course(0, "name", "description", "department", 10, new Term("Fall", 2018),
                'A', "instructor", meetingTimes, 3);

        assertEquals(3, course.getCredits());
    }

    @Test
    void testEquals() {
        ArrayList<Queue<Course>> allCourses = getAllCoursesFromExcel();
        ArrayList<Queue<Course>> allCourses2 = getAllCoursesFromExcel();
        while (allCourses.get(0).peek() != null){
            assertEquals(allCourses.get(0).poll().equals(allCourses2.get(0).poll()), true);
        }

        while ((allCourses.get(1).peek() != null)&&(allCourses2.get(2).peek() != null)){
            assertEquals(allCourses.get(1).poll().equals(allCourses2.get(2).poll()), false);
        }

        ArrayList<Course> courseTest = new ArrayList<>();
        while (allCourses.get(2).peek() != null){
            courseTest.add(allCourses.get(2).poll());
        }

        while (allCourses2.get(1).peek() != null){
            Course testingCourse = allCourses2.get(1).poll();
            for (Course course : courseTest){
                assertEquals(course.equals(testingCourse), false);
            }
        }
    }

    public ArrayList<Queue<Course>> getAllCoursesFromExcel(){
        Queue<Course> courses2019 = new LinkedList<>();
        Queue<ArrayList<String>> data2019 = Excel.csv.read("2018-2019.csv");
        data2019.poll();
        while (data2019.peek() != null) {
            ArrayList<String> line = data2019.poll();
            courses2019.add(new Course(line));
        }

        Queue<Course> courses2020 = new LinkedList<>();
        Queue<ArrayList<String>> data2020 = Excel.csv.read("2019-2020.csv");
        data2020.poll();
        while (data2020.peek() != null) {
            ArrayList<String> line = data2020.poll();
            courses2020.add(new Course(line));
        }

        Queue<Course> courses2021 = new LinkedList<>();
        Queue<ArrayList<String>> data2021 = Excel.csv.read("2020-2021.csv");
        data2021.poll();
        while (data2021.peek() != null) {
            ArrayList<String> line = data2021.poll();
            courses2021.add(new Course(line));
        }

        ArrayList<Queue<Course>> out = new ArrayList<>();
        out.add(courses2019);
        out.add(courses2020);
        out.add(courses2021);

        return out;
    }
}