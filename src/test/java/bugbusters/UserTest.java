package bugbusters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void addUserMajor() {
        //TODO: check duplicates
        User user1 = new User();
        String majorName = "B.S. in Computer Science";
        int reqYr = 2022;

        user1.addUserMajor(majorName,reqYr);
        assertEquals(1, user1.getUserMajors().size());

        majorName = "B.S. in Business Statistics";
        reqYr = 2025;
        user1.addUserMajor(majorName,reqYr);
        assertEquals(1, user1.getUserMajors().size());

        user1.addUserMajor("B.S. in Social Work", 2023);
        assertEquals(2,user1.getUserMajors().size());

        user1.addUserMajor("B.S. in Biology",2022);
        assertEquals(2,user1.getUserMajors().size());
    }

    @Test
    void removeUserMajor() {
        User user = new User();
        user.addUserMajor("B.S. in Computer Science",2022);
        user.addUserMajor("B.S. in Business Statistics",2023);
        assertEquals(2,user.getUserMajors().size());

        user.removeUserMajor("B.S. in Computer Science");
        assertEquals(1,user.getUserMajors().size());
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