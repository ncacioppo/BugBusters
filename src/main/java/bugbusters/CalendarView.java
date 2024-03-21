package bugbusters;

import com.mysql.cj.util.StringUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CalendarView {
    private Schedule schedule;
    private final int START_HOUR = 7;
    private final int END_HOUR = 9;
    private ArrayList<LocalTime> hoursOfTheDay;
    private ArrayList<String> daysOfTheWeek;

    public CalendarView(Schedule schedule) {
        this.daysOfTheWeek = new ArrayList<>(List.of("Mon","Tue","Wed","Thu","Fri"));
        this.hoursOfTheDay = setHoursOfTheDay();
        this.schedule = schedule;
    }

    public void printDaysOfTheWeek() {
        System.out.print("     "); //5 spaces
        for(String day : this.daysOfTheWeek) {
            System.out.print(StringUtils.padString(day,15));
        }
        System.out.println();
    }

    private ArrayList<LocalTime> setHoursOfTheDay() {
        ArrayList<LocalTime> hours = new ArrayList<>();

        for(int i = START_HOUR; i <= END_HOUR; i++) {
            hours.add(LocalTime.of(7,0,0));
        }

        return hours;
    }

    public void printScheduleAsCalendar() {
        printDaysOfTheWeek();

        for (LocalTime hour : this.hoursOfTheDay) {    //can replace with more granular time
                                                        // or while loop at smaller increment
            System.out.printf("%dam", hour.getHour());
            for (String day : this.daysOfTheWeek) {
                System.out.print(StringUtils.padString("-",15));
//                for (Course course : this.schedule.courses) {
//                    for (MeetingTime meetingTime : course.getMeetingTimes()) {
//                        if(day.equals(meetingTime.getDay().toString())) {
//                            LocalTime courseStartingHr = meetingTime.getStartTime();
////                            LocalTime courseEndingHr = meetingTime.getEndTime();
//                        }
//                    }
//                }
            }
            System.out.println();
        }
    }

}
