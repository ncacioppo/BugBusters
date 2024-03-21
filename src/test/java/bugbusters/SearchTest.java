package bugbusters;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
        assertTrue(search.byDepartment(courses, "Z").size() == 0);
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
    }

    @Test
    void withinTime() {
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