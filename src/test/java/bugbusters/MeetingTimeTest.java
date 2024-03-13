package bugbusters;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class MeetingTimeTest {
    LocalTime startTime1 = LocalTime.of(10, 0);
    LocalTime startTime2 = LocalTime.of(12, 30);
    LocalTime startTime3 = LocalTime.of(2, 0);

    LocalTime endTime1 = LocalTime.of(10 ,50);
    LocalTime endTime2 = LocalTime.of(1, 45);
    LocalTime endTime3 = LocalTime.of(2, 50);

    Day day1 = Day.MONDAY;
    Day day2 = Day.THURSDAY;
    Day day3 = Day.FRIDAY;

    MeetingTime mt1 = new MeetingTime(day1, startTime1, endTime1);
    MeetingTime mt2 = new MeetingTime(day2, startTime2, endTime2);
    MeetingTime mt3 = new MeetingTime(day3, startTime3, endTime3);
    @Test
    void getDay() {
        Day testDay = mt1.getDay();
        assertEquals(Day.MONDAY, testDay);
        Day testDay2 = mt2.getDay();
        assertNotEquals(Day.MONDAY, testDay2);
    }

    @Test
    void getStartTime() {
    }

    @Test
    void getEndTime() {
    }

    @Test
    void getDuration() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testToString() {
    }

    @Test
    void compareTo() {
    }
}