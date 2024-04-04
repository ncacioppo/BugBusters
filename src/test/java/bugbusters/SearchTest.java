package bugbusters;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {
    @Test
    void getAllCourses() {
        Search search = new Search();

        List<Course> courses = search.getAllCoursesFromExcel();

        assertEquals(4526, courses.size());

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
        Search search = new Search();

        List<Course> courses = new ArrayList<>();

        courses.add(new Course(1, "physics", "101", "PHYS", 38888, new Term("SPRING", 2024), 'A', "Brower", null, 4));
        courses.add(new Course(1, "physics", "101", "PHYS", 38888, new Term("SPRING", 2024), 'A', "Brower", null, 4));
        courses.add(new Course(2, "calc", "163", "MATH", 39000, new Term("FALL", 2024), 'A', "McIntyre", null, 4));
        courses.add(new Course(2, "calc", "163", "MATH", 39500, new Term("SPRING", 2023), 'A', "McIntyre", null, 4));


        assertEquals(4, search.byCode(courses, "38000-40000").size());
        assertEquals(3, search.byCode(courses, "38000-39000").size());
        assertEquals(4, search.byCode(courses, "38888-39500").size());
        assertEquals(1, search.byCode(courses, "38889-39499").size());
        assertEquals(1, search.byCode(courses, "39000-39000").size());
        assertEquals(0, search.byCode(courses, "39600-40000").size());
    }

    @Test
    void byTerm() {
        Search search = new Search();

        List<Course> courses = new ArrayList<>();

        courses.add(new Course(1, "physics", "101", "PHYS", 38888, new Term("SPRING", 2024), 'A', "Brower", null, 4));
        courses.add(new Course(1, "physics", "101", "PHYS", 38888, new Term("SPRING", 2024), 'A', "Brower", null, 4));
        courses.add(new Course(2, "calc", "163", "MATH", 38889, new Term("FALL", 2024), 'A', "McIntyre", null, 4));
        courses.add(new Course(2, "calc", "163", "MATH", 38889, new Term("SPRING", 2023), 'A', "McIntyre", null, 4));

        Term term1 = new Term("SPRING", 2024);
        Term term2 = new Term("FALL", 2024);
        Term term3 = new Term("SPRING", 2023);
        Term term4 = new Term("FALL", 2023);
        Term term5 = new Term("SPRING", 2022);
        Term term6 = new Term("FALL", 2022);

        assertTrue(search.byTerm(courses, term1.toString()).size() == 2);
        assertTrue(search.byTerm(courses, term2.toString()).size() == 1);
        assertTrue(search.byTerm(courses, term3.toString()).size() == 1);
        assertTrue(search.byTerm(courses, term4.toString()).size() == 0);
        assertTrue(search.byTerm(courses, term5.toString()).size() == 0);
        assertTrue(search.byTerm(courses, term6.toString()).size() == 0);
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
        Search search = new Search();

        List<Course> courses = new ArrayList<>();
        List<Course> outcome1 = new ArrayList<>();
        List<Course> outcome2 = new ArrayList<>();

        courses.add(new Course(1, "physics", "101", "PHYS", 38888, null, 'A', "Brower", null, 4));
        courses.add(new Course(2, "physics", "101", "PHYS", 38888, null, 'A', "Brower", null, 4));
        courses.add(new Course(2, "calc", "163", "MATH", 38889, null, 'A', "McIntyre", null, 4));
        courses.add(new Course(3, "calc", "163", "MATH", 38889, null, 'A', "McIntyre", null, 4));

        assertTrue(search.byID(courses, "00001").size() == 1);
        assertTrue(search.byID(courses, "00002").size() == 2);
        assertTrue(search.byID(courses, "00003").size() == 1);
        assertTrue(search.byID(courses, "00004").size() == 0);
    }

    @Test
    void byDay() {
        Search search = new Search();

        List<Course> courses = new ArrayList<>();

        Set<MeetingTime> firstCourseTimes = new HashSet<>();
        Set<MeetingTime> secondCourseTimes = new HashSet<>();

        Term courseTerm = new Term("SPRING", 2024);

        firstCourseTimes.add(new MeetingTime("MONDAY 10:00:00 to 10:50:00"));
        firstCourseTimes.add(new MeetingTime("WEDNESDAY 10:00:00 to 10:50:00"));
        firstCourseTimes.add(new MeetingTime("FRIDAY 10:00:00 to 10:50:00"));
        secondCourseTimes.add(new MeetingTime("MONDAY 13:00:00 to 13:50:00"));
        secondCourseTimes.add(new MeetingTime("TUESDAY 13:00:00 to 13:50:00"));
        secondCourseTimes.add(new MeetingTime("WEDNESDAY 13:00:00 to 13:50:00"));
        secondCourseTimes.add(new MeetingTime("FRIDAY 13:00:00 to 13:50:00"));

        courses.add(new Course(1, "physics", "101", "PHYS", 38888, courseTerm, 'A', "Brower", new ArrayList<>(firstCourseTimes), 4));
        courses.add(new Course(2, "calc", "163", "MATH", 38889, courseTerm, 'A', "McIntyre", new ArrayList<>(secondCourseTimes), 4));

        assertTrue(search.byDay(courses, "Monday").size() == 2);
        assertTrue(search.byDay(courses, "Tuesday").size() == 1);
        assertTrue(search.byDay(courses, "Wednesday").size() == 2);
        assertTrue(search.byDay(courses, "Thursday").size() == 0);
        assertTrue(search.byDay(courses, "Friday").size() == 2);
        assertTrue(search.byDay(courses, "").size() == 2);
        assertTrue(search.byDay(courses, "Z").size() == 0);
    }

    @Test
    void withinTime() {
        Search search = new Search();

        List<Course> courses = new ArrayList<>();

        Set<MeetingTime> firstCourseTimes = new HashSet<>();
        Set<MeetingTime> secondCourseTimes = new HashSet<>();

        Term courseTerm = new Term("SPRING", 2024);

        firstCourseTimes.add(new MeetingTime("MONDAY 10:00:00 to 10:50:00"));
        firstCourseTimes.add(new MeetingTime("WEDNESDAY 10:00:00 to 10:50:00"));
        firstCourseTimes.add(new MeetingTime("FRIDAY 10:00:00 to 10:50:00"));
        secondCourseTimes.add(new MeetingTime("MONDAY 13:00:00 to 13:50:00"));
        secondCourseTimes.add(new MeetingTime("TUESDAY 13:00:00 to 13:50:00"));
        secondCourseTimes.add(new MeetingTime("WEDNESDAY 13:00:00 to 13:50:00"));
        secondCourseTimes.add(new MeetingTime("FRIDAY 13:00:00 to 13:50:00"));

        courses.add(new Course(1, "physics", "101", "PHYS", 38888, courseTerm, 'A', "Brower", new ArrayList<>(firstCourseTimes), 4));
        courses.add(new Course(2, "calc", "163", "MATH", 38889, courseTerm, 'A', "McIntyre", new ArrayList<>(secondCourseTimes), 4));

        assertTrue(search.withinTime(courses, "10:00:00-15:00:00").size() == 2);
        assertTrue(search.withinTime(courses, "10:00:00-10:50:00").size() == 1);
        assertTrue(search.withinTime(courses, "10:00:00-10:30:00").size() == 0);
        assertTrue(search.withinTime(courses, "10:30:00-13:50:00").size() == 1);
        assertTrue(search.withinTime(courses, "13:00:00-13:50:00").size() == 1);
        assertTrue(search.withinTime(courses, "13:00:00-13:01:00").size() == 0);
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