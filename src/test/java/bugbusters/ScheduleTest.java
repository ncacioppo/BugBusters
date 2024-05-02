package bugbusters;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    @Test
    void undoChange() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Day");


        ArrayList<Course> courses = new ArrayList<>();
        Term term = new Term("SPRING", 2024);

        Schedule test = new Schedule(user, "Test Schedule", term, courses);

        Search search = new Search();
        ArrayList<Course> results = (ArrayList<Course>) search.byName(search.getAllCoursesFromExcel(), "Automata Theory");
        test.addCourse(results.get(0));

        System.out.println(test.toString());

        System.out.println(test.undoChange().shortToString());
        System.out.println();
        System.out.println(test.toString());

        user.getRegistrar().disconnectFromDB();
    }

    @Test
    void redoChange() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Day");


        ArrayList<Course> courses = new ArrayList<>();
        Term term = new Term("SPRING", 2024);

        Schedule test = new Schedule(user, "Test Schedule", term, courses);

        Search search = new Search();
        ArrayList<Course> results = (ArrayList<Course>) search.byName(search.getAllCoursesFromExcel(), "Automata Theory");
        test.addCourse(results.get(0));

        System.out.println(test.toString());

        System.out.println(test.undoChange().shortToString());
        System.out.println();
        System.out.println(test.toString());
        System.out.println();
        System.out.println(test.redoChange().shortToString());
        System.out.println();
        System.out.println(test.toString());

        user.getRegistrar().disconnectFromDB();
    }

    @Test
    void findConflict() {
        Search search = new Search();
        ArrayList<Course> results = (ArrayList<Course>) search.byDepartment(search.getAllCoursesFromExcel(),"HUMA");
        Course course1 = results.getFirst();
        Course course2 = results.get(1);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Day");

        ArrayList<Course> courses = new ArrayList<>();
        Term term = new Term("SPRING", 2024);

        Schedule test = new Schedule(user, "Test Schedule", term, courses);

        test.addCourse(course2);
        test.findConflict(course1);

        System.out.println(test.currentConflict.getKey().shortToString());

        user.getRegistrar().disconnectFromDB();
    }

    @Test
    void resolveConflict() {
        // TODO: test upon having the UI being integrated to the backend
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
    void isValid() {
    }
}