package bugbusters;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {
    @Test
    void getAllCourses() {

    }

    @Test
    void removeFilter() {
    }

    @Test
    void byKeyword() {
        Search search = new Search();

        List<Course> courses = new ArrayList<>();
        List<Course> outcome1 = new ArrayList<>();
        List<Course> outcome2 = new ArrayList<>();

        courses.add(new Course(1, "physics", "101 kinematics torque", "PHYS", 38888, null, 'A', "Brower", null, 4));
        outcome1.add(new Course(1, "physics", "101 kinematics torque", "PHYS", 38888, null, 'A', "Brower", null, 4));
        courses.add(new Course(2, "calc", "163 triple integrals multivariable", "MATH", 38889, null, 'A', "McIntyre", null, 4));
        outcome2.add(new Course(2, "calc", "163 triple integrals multivariable", "MATH", 38889, null, 'A', "McIntyre", null, 4));

        assertTrue(search.byKeyword(courses, "math").size() == 1);
        assertTrue(search.byKeyword(courses, "Physics").size() == 1);
        assertTrue(search.byKeyword(courses, "").size() == 2);
        assertTrue(search.byKeyword(courses, "wuefvgay8fgeyrubvhzud").size() == 0);
        assertTrue(search.byKeyword(courses, "kine").size() == 1);
        assertTrue(search.byKeyword(courses, "integ").size() == 1);
    }

    @Test
    void byName() {
        Search search = new Search();

        List<Course> courses = new ArrayList<>();
        List<Course> outcome1 = new ArrayList<>();
        List<Course> outcome2 = new ArrayList<>();

        courses.add(new Course(1, "physics", "101", "PHYS", 38888, null, 'A', "Brower", null, 4));
        outcome1.add(new Course(1, "physics", "101", "PHYS", 38888, null, 'A', "Brower", null, 4));
        courses.add(new Course(2, "calc", "163", "MATH", 38889, null, 'A', "McIntyre", null, 4));
        outcome2.add(new Course(2, "calc", "163", "MATH", 38889, null, 'A', "McIntyre", null, 4));

        assertTrue(search.byName(courses, "Physics").size() == 1);
        assertTrue(search.byName(courses, "Calc").size() == 1);
        assertTrue(search.byName(courses, "phys").size() == 1);
        assertTrue(search.byName(courses, "cal").size() == 1);
        assertTrue(search.byName(courses, "").size() == 2);
        assertTrue(search.byName(courses, "X").size() == 0);
    }

    @Test
    void byDepartment() {
        Search search = new Search();

        List<Course> courses = new ArrayList<>();
        List<Course> outcome1 = new ArrayList<>();
        List<Course> outcome2 = new ArrayList<>();

        courses.add(new Course(1, "physics", "101", "PHYS", 38888, null, 'A', "Brower", null, 4));
        outcome1.add(new Course(1, "physics", "101", "PHYS", 38888, null, 'A', "Brower", null, 4));
        courses.add(new Course(2, "calc", "163", "MATH", 38889, null, 'A', "McIntyre", null, 4));
        outcome2.add(new Course(2, "calc", "163", "MATH", 38889, null, 'A', "McIntyre", null, 4));

        assertTrue(search.byDepartment(courses, "PHYS").size() == 1);
        assertTrue(search.byDepartment(courses, "MATH").size() == 1);
        assertTrue(search.byDepartment(courses, "").size() == 2);
        assertTrue(search.byDepartment(courses, "R").size() == 0);
    }

    @Test
    void byCode() {

    }

    @Test
    void byTerm() {

    }

    @Test
    void byProfessor() {
        Search search = new Search();

        List<Course> courses = new ArrayList<>();
        List<Course> outcome1 = new ArrayList<>();
        List<Course> outcome2 = new ArrayList<>();

        courses.add(new Course(1, "physics", "101", "PHYS", 38888, null, 'A', "Brower", null, 4));
        outcome1.add(new Course(1, "physics", "101", "PHYS", 38888, null, 'A', "Brower", null, 4));
        courses.add(new Course(2, "calc", "163", "MATH", 38889, null, 'A', "McIntyre", null, 4));
        outcome2.add(new Course(2, "calc", "163", "MATH", 38889, null, 'A', "McIntyre", null, 4));

        assertTrue(search.byProfessor(courses, "McIntyre").size() == 1);
        assertTrue(search.byProfessor(courses, "Brower").size() == 1);
        assertTrue(search.byProfessor(courses, "").size() == 2);
        assertTrue(search.byProfessor(courses, "Z").size() == 0);
    }

    @Test
    void byID() {

    }

    @Test
    void byDay() {
        Search search = new Search();
        Set<MeetingTime> firstClassMeetingTimes = new HashSet<MeetingTime>();
        Set<MeetingTime> secondClassMeetingTimes = new HashSet<MeetingTime>();

        firstClassMeetingTimes.add(new MeetingTime("MONDAY 10:00 to 10:50"));
        firstClassMeetingTimes.add(new MeetingTime("WEDNESDAY 10:00 to 10:50"));
        firstClassMeetingTimes.add(new MeetingTime("FRIDAY 10:00 to 10:50"));
        secondClassMeetingTimes.add(new MeetingTime("MONDAY 13:00 to 13:50"));
        secondClassMeetingTimes.add(new MeetingTime("TUESDAY 13:00 to 13:50"));
        secondClassMeetingTimes.add(new MeetingTime("WEDNESDAY 13:00 to 13:50"));
        secondClassMeetingTimes.add(new MeetingTime("FRIDAY 13:00 to 13:50"));


        List<Course> courses = new ArrayList<>();
        List<Course> outcome1 = new ArrayList<>();
        List<Course> outcome2 = new ArrayList<>();

        courses.add(new Course(1, "physics", "101", "PHYS", 38888, null, 'A', "Brower", firstClassMeetingTimes, 4));
        outcome1.add(new Course(1, "physics", "101", "PHYS", 38888, null, 'A', "Brower", firstClassMeetingTimes, 4));
        courses.add(new Course(2, "calc", "163", "MATH", 38889, null, 'A', "McIntyre", secondClassMeetingTimes, 4));
        outcome2.add(new Course(2, "calc", "163", "MATH", 38889, null, 'A', "McIntyre", secondClassMeetingTimes, 4));

        assertTrue(search.byDay(courses, "Monday").size() == 2);
        assertTrue(search.byDay(courses, "Tuesday").size() == 1);
        assertTrue(search.byDay(courses, "Wednesday").size() == 2);
        assertTrue(search.byDay(courses, "Thursday").size() == 0);
        assertTrue(search.byDay(courses, "Friday").size() == 2);
        assertTrue(search.byDay(courses, "Z").size() == 0);
        assertTrue(search.byDay(courses, "").size() == 2);
    }

    @Test
    void withinTime() {
        Search search = new Search();
        Set<MeetingTime> firstClassMeetingTimes = new HashSet<MeetingTime>();
        Set<MeetingTime> secondClassMeetingTimes = new HashSet<MeetingTime>();

        firstClassMeetingTimes.add(new MeetingTime("MONDAY 10:00:00 to 10:50:00"));
        firstClassMeetingTimes.add(new MeetingTime("WEDNESDAY 10:00:00 to 10:50:00"));
        firstClassMeetingTimes.add(new MeetingTime("FRIDAY 10:00:00 to 10:50:00"));
        secondClassMeetingTimes.add(new MeetingTime("MONDAY 13:00:00 to 13:50:00"));
        secondClassMeetingTimes.add(new MeetingTime("TUESDAY 13:00:00 to 13:50:00"));
        secondClassMeetingTimes.add(new MeetingTime("WEDNESDAY 13:00:00 to 13:50:00"));
        secondClassMeetingTimes.add(new MeetingTime("FRIDAY 13:00:00 to 13:50:00"));

        List<Course> courses = new ArrayList<>();
        List<Course> outcome1 = new ArrayList<>();
        List<Course> outcome2 = new ArrayList<>();

        courses.add(new Course(1, "physics", "101", "PHYS", 38888, null, 'A', "Brower", firstClassMeetingTimes, 4));
        outcome1.add(new Course(1, "physics", "101", "PHYS", 38888, null, 'A', "Brower", firstClassMeetingTimes, 4));
        courses.add(new Course(2, "calc", "163", "MATH", 38889, null, 'A', "McIntyre", secondClassMeetingTimes, 4));
        outcome2.add(new Course(2, "calc", "163", "MATH", 38889, null, 'A', "McIntyre", secondClassMeetingTimes, 4));

        assertTrue(search.withinTime(courses, "10:00:00", "15:00:00").size() == 2);
        assertTrue(search.withinTime(courses, "10:00:00", "10:50:00").size() == 1);
        assertTrue(search.withinTime(courses, "10:00:00", "10:30:00").size() == 0);
        assertTrue(search.withinTime(courses, "13:00:00", "13:01:00").size() == 0);
        assertTrue(search.withinTime(courses, "13:00:00", "13:50:00").size() == 1);
    }

    @Test
    void testEquals() {
    }

    @Test
    void testToString() {
    }

    @Test
    void getFilters() {
    }

    @Test
    void setFilters() {
    }
}