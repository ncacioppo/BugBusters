package bugbusters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void getEmptyCollegeYear() {
        User user = new User();
        assertEquals(null,user.getCollegeYear());
    }

    @Test
    void getCollegeYear() {
        User user1 = new User();
        user1.setCollegeYear(CollegeYear.JUNIOR);
        assertEquals(CollegeYear.JUNIOR,user1.getCollegeYear());

        user1.setCollegeYear(CollegeYear.SENIOR);
        assertEquals("SENIOR",user1.getCollegeYear().toString());

        User user2 = new User();
        user2.setCollegeYear("freshman");
        assertEquals(CollegeYear.FRESHMAN, user2.getCollegeYear());
    }

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
    void addUserMinor() {
        User user1 = new User();
        String minorName = "Computer Science";
        int reqYr = 2018;

        user1.addUserMinor(minorName,reqYr);
        assertEquals(1, user1.getUserMinors().size());

        minorName = "Philosophy";
        reqYr = 2025;
        user1.addUserMinor(minorName,reqYr);
        assertEquals(1, user1.getUserMinors().size());

        user1.addUserMinor("Philosophy", 2019);
        assertEquals(2,user1.getUserMinors().size());

        user1.addUserMinor("Cybersecurity",2020);
        assertEquals(3,user1.getUserMinors().size());

        user1.addUserMinor("Cybersecurity",2020);
        assertEquals(3,user1.getUserMinors().size());

        user1.addUserMinor("Pre-Law",2018);
        assertEquals(4,user1.getUserMinors().size());

        user1.addUserMinor("Psychology",2019);
        assertEquals(4,user1.getUserMinors().size());
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