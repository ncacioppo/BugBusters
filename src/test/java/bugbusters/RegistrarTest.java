package bugbusters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrarTest {

    @Test
    void getMajors() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");

        for (String major : registrar.getMajors()) {
            System.out.println(major);
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
    void getMinors() {
    }

    @Test
    void getCourses() {
    }
}