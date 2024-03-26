package bugbusters;

import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class RegistrarTest {

    @Test
    void parseTimeAttribute() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");

        Time expected = Time.valueOf(LocalTime.of(16,0,0));
        Time actual = registrar.parseTimeAttribute("1/1/1900 16:00", "2018-2019_GCC_Courses.csv");

        assertEquals(expected.getTime(),actual.getTime());
    }

    @Test
    void deleteCourseByID() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        registrar.deleteCourse(184892);
    }

    @Test
    void insertCoursesFromCSV() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        int insertedRows = registrar.insertCoursesFromCSV("2018-2019_GCC_Courses.csv");
        System.out.println("Inserted Rows: " + insertedRows);
    }

    @Test
    void getMajors() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");

        for (String major : registrar.getMajors()) {
            System.out.println(major);
        }
    }
    @Test
    void getMinors() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");

        for (String minor : registrar.getMinors()) {
            System.out.println(minor);
        }
    }

    @Test
    void getReqYrs() {
    }

    @Test
    void isMajor() {
    }

    @Test
    void isReqYr() {
    }

    @Test
    void getCourses() {
    }
}