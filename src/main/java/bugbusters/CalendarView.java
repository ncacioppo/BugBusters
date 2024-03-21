package bugbusters;

import com.mysql.cj.util.StringUtils;

import java.text.Format;
import java.time.LocalTime;
import java.util.*;


public class CalendarView {
    private Schedule schedule;
    private final int START_HOUR = 7;
    private final int END_HOUR = 21;
    private ArrayList<LocalTime> hoursOfTheDay;
    private ArrayList<String> daysOfTheWeek;

    public CalendarView(Schedule schedule) {
        this.daysOfTheWeek = new ArrayList<>(List.of("Mon","Tue","Wed","Thu","Fri"));
        this.hoursOfTheDay = setHoursOfTheDay();
        this.schedule = schedule;
    }

    public void printDaysOfTheWeek() {
        System.out.print("               ");             //15 spaces
        for(String day : this.daysOfTheWeek) {
            System.out.print(StringUtils.padString(day,15));
        }
        System.out.println();
    }

    private ArrayList<LocalTime> setHoursOfTheDay() {
        ArrayList<LocalTime> hours = new ArrayList<>();

        for(int i = START_HOUR; i <= END_HOUR; i++) {
            hours.add(LocalTime.of(i,0,0));
        }

        return hours;
    }

    public void printScheduleAsCalendar() {
        printDaysOfTheWeek();

        for (LocalTime hour : this.hoursOfTheDay) {    //can replace with more granular time
                                                        // or while loop at smaller increment
            int hr_int = hour.getHour();
            if(hr_int < 12) {
                System.out.printf("%dam", hr_int);
            } else if (hr_int == 12) {
                System.out.printf("%dpm", hr_int);
            }
            else {
                System.out.printf("%dpm", hr_int - 12);
            }
            for (String day : this.daysOfTheWeek) {

                System.out.print(StringUtils.padString("-",5));
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
