package bugbusters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void addUserMajor() {
        User user1 = new User();
        String majorName = "B.S. in Computer Science";
        int reqYr = 2022;

        user1.addUserMajor(majorName,reqYr);
        assertEquals(1, user1.getMajors().size());
    }

    @Test
    void getFirstName() {
    }

    @Test
    void getLastName() {
    }

    @Test
    void getMajors() {
    }

    @Test
    void getMinors() {
    }
}