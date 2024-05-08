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

        expected = Time.valueOf(LocalTime.of(15,50,0));
        actual = registrar.parseTimeAttribute("3:50:00 PM", "2019-2020_GCC_Courses.csv");

        assertEquals(expected.getTime(),actual.getTime());
    }

    @Test
    void deleteCourseByID() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        registrar.deleteCourse(184892);
    }

    @Test
    void insertCoursesFromCSV() {
        int insertedRows = 0;
        boolean idGenerated = false;
        //Only needs to be run once. To delete rows, drop and recreate course table or create a new method
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        insertedRows += registrar.insertCoursesFromCSV("2018-2019_GCC_Courses.csv");
        insertedRows += registrar.insertCoursesFromCSV("2019-2020_GCC_Courses.csv");
        insertedRows += registrar.insertCoursesFromCSV("2020-2021_GCC_Courses.csv");
        idGenerated = registrar.generateIdAttribute("course");
        registrar.disconnectFromDB();

        System.out.println("Inserted Rows: " + insertedRows);
        System.out.println("Generated ID column: " + idGenerated);
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
        // 2020, 2021, 2022, 2023, and 2024 will all be considered valid requirement years
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");

        assertFalse(registrar.isReqYr(2018));
        assertFalse(registrar.isReqYr(2019));
        assertTrue(registrar.isReqYr(2020));
        assertTrue(registrar.isReqYr(2021));
        assertTrue(registrar.isReqYr(2022));
        assertTrue(registrar.isReqYr(2023));
        assertTrue(registrar.isReqYr(2024));
        assertFalse(registrar.isReqYr(2025));
        assertFalse(registrar.isReqYr(-3492));
        assertFalse(registrar.isReqYr(0));
    }

    @Test
    void getCourses() {
    }
}