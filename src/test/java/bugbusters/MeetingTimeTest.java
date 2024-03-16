package bugbusters;

import org.junit.jupiter.api.Test;

import java.time.Duration;
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

    MeetingTime mt4 = new MeetingTime("MONDAY 10:00 to 10:50");
    @Test
    void getDay() {
        Day testDay = mt1.getDay();
        assertEquals(Day.MONDAY, testDay);
        Day testDay2 = mt2.getDay();
        assertNotEquals(Day.MONDAY, testDay2);
        Day testDay3 = mt4.getDay();
        assertEquals(Day.MONDAY, testDay3);
    }

    @Test
    void getStartTime() {
        LocalTime testStartTime = mt1.getStartTime();
        assertEquals(LocalTime.of(10, 0), testStartTime);
        assertNotEquals(LocalTime.of(11, 0), testStartTime);

        LocalTime testStartTime2 = mt4.getStartTime();
        assertEquals(LocalTime.of(10, 0), testStartTime2);
    }

    @Test
    void getEndTime() {
        LocalTime testEndTime = mt1.getEndTime();
        assertEquals(LocalTime.of(10, 50), testEndTime);
        assertNotEquals(LocalTime.of(11, 50), testEndTime);

        LocalTime testEndTime2 = mt4.getEndTime();
        assertEquals(LocalTime.of(10, 50), testEndTime2);
    }

    @Test
    void getDuration() {
        Duration duration1 = Duration.between(mt1.getStartTime(), mt1.getEndTime());
        assertEquals(duration1, mt1.getDuration());

        Duration duration2 = Duration.between(mt4.getStartTime(), mt4.getEndTime());
        assertEquals(duration2, mt4.getDuration());
    }

    @Test
    void testEquals() {
        assertEquals(mt1, mt4);
        assertNotEquals(mt1, mt3);
    }

    @Test
    void testToString() {
        String expected = "MONDAY 10:00 to 10:50";
        assertEquals(expected, mt1.toString());
        String unexpected = "MONDAY 1000 to 1050";
        assertNotEquals(unexpected, mt3.toString());
    }

    @Test
    void compareTo() {
        assertEquals(1, mt2.compareTo(mt1));
        assertEquals(-1, mt1.compareTo(mt2));
        assertEquals(0, mt1.compareTo(mt4));
    }
}