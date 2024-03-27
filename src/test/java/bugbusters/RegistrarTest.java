package bugbusters;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RegistrarTest {

    @Test
    void getMajors() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");

        for (String major : registrar.getMajors()) {
            System.out.println(major);
        }
        registrar.disconnectFromDB();
    }

    @Test
    void getReqYrsFromCurrentYear() {
        //TODO: check db requirement years
        int[] currReqYrs = new int[6];  //2019,2020,2021,2022,2023,2024
        int precYr = 2019;
        for(int i = 0; i < currReqYrs.length; i++) {
            currReqYrs[i] = precYr;
            precYr++;
        }

        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        registrar.setReqYrsFromCurrent();

        assertEquals(currReqYrs.length, registrar.getReqYrs().length);
        for(int i = 0; i < currReqYrs.length; i++) {
            assertEquals(currReqYrs[i], registrar.getReqYrs()[i]);
        }
        registrar.disconnectFromDB();
    }

    @Test
    void getReqYrsFromDB() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        int[] expYears = new int[2];
        expYears[0] = 2018;
        expYears[1] = 2020;

        int[] actualYears = registrar.getCourseYearsFromDB();
        for(int i = 0; i < expYears.length; i++) {
            assertEquals(expYears[i], actualYears[i]);
        }
        registrar.disconnectFromDB();
    }

    @Test
    void isMajor() {
    }

    @Test
    void isReqYr() {
    }

    @Test
    void getMinors() {
    }

    @Test
    void getCourses() {
    }
}