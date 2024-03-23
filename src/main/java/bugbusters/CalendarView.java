package bugbusters;

import com.mysql.cj.util.StringUtils;

import java.text.Format;
import java.time.LocalTime;
import java.util.*;


public class CalendarView {
    private Schedule schedule;
    private final int START_HOUR = 7;
    private final int END_HOUR = 17;
    private ArrayList<LocalTime> hoursOfTheDay;
    private ArrayList<String> daysOfTheWeek;

    public CalendarView(Schedule schedule) {
        this.daysOfTheWeek = new ArrayList<>(List.of("MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY"));
        this.hoursOfTheDay = setHoursOfTheDay();
        this.schedule = schedule;
    }

    public void printDaysOfTheWeek() {
        System.out.format("%10s","");             //15 spaces
        for(String day : this.daysOfTheWeek) {
            if(day.equals("MONDAY")) {
                System.out.format("%-10s","Mon");
            }
            if(day.equals("TUESDAY")) {
                System.out.format("%6s","Tue");
            }
            if(day.equals("WEDNESDAY")) {
                System.out.format("%13s","Wed");
            }
            if(day.equals("THURSDAY")) {
                System.out.format("%13s","Thu");
            }
            if(day.equals("FRIDAY")) {
                System.out.format("%13s","Fri");
            }
        }
        System.out.println();
    }

    private ArrayList<LocalTime> setHoursOfTheDay() {
        ArrayList<LocalTime> hours = new ArrayList<>();
//        int latestHourInSchedule = schedule.getLatestHour();      //TODO: fix schedule == null bug

        //add hours between START_HOUR and END_HOUR, inclusive
        for(int i = START_HOUR; i <= END_HOUR; i++) {
            hours.add(LocalTime.of(i,0,0));
        }
        //if latest class exceeds 5pm, add more hours
        //This is for expanding the calendar view
//        if (latestHourInSchedule > 17) {
//            for(int i = END_HOUR; i <= latestHourInSchedule + 1; i++) {
//                hours.add(LocalTime.of(i, 0, 0));
//            }
//        }

        return hours;
    }

    public void printScheduleAsCalendar() {
        int hr_int;                                 //acts as hour counter
        printDaysOfTheWeek();

        for (LocalTime hour : this.hoursOfTheDay) {    //can replace with more granular time
                                                        // or while loop at smaller increment
            hr_int = hour.getHour();
            if(hr_int < 12) {
                System.out.printf("%2d am", hr_int);
            } else if (hr_int == 12) {
                System.out.printf("%2d pm", hr_int);
            }
            else {
                System.out.printf("%2d pm", hr_int - 12);
            }
            for (String day : this.daysOfTheWeek) {
//                System.out.format("%6s","-");   //if monday
                for (Course course : this.schedule.getCourses()) {
                    for (MeetingTime meetingTime : course.getMeetingTimes()) {
                        if (day.equals(meetingTime.getDay().toString())) {
                            int courseStartingHr = meetingTime.getStartTime().getHour();
                            int courseEndingHr = meetingTime.getEndTime().getHour();
                            if ((hr_int >= courseStartingHr) && (hr_int <= courseEndingHr)) {
                                System.out.format("%9s %3d",course.getDepartment(),course.getCode());
                            } else {
                                System.out.format("%9s %3s","    ", "   ");
                            }
                        }
                    }
                }
            }
            System.out.println();
        }
    }

}
