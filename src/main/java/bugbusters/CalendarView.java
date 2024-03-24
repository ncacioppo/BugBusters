package bugbusters;

import com.mysql.cj.util.StringUtils;

import java.text.Format;
import java.time.LocalTime;
import java.util.*;


public class CalendarView {
    private Schedule schedule;
    private final int START_HOUR = 7;
    private final int END_HOUR = 16;
    private ArrayList<LocalTime> hoursOfTheDay;
    private ArrayList<LocalTime> halfHoursOfTheDay;
    private ArrayList<String> daysOfTheWeek;

    public CalendarView(Schedule schedule) {
        this.daysOfTheWeek = new ArrayList<>(List.of("MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY"));
        this.hoursOfTheDay = setHoursOfTheDay();
        this.halfHoursOfTheDay = setHalfHoursOfTheDay();
        this.schedule = schedule;
    }

    private ArrayList<LocalTime> setHalfHoursOfTheDay() {
        ArrayList<LocalTime> halfHours = new ArrayList<>();
        ArrayList<Integer> increments = new ArrayList<>(List.of(0,30));

        for(int i = START_HOUR; i <= END_HOUR; i++) {
            for(int j = 0; j < increments.size(); j++) {
                halfHours.add(LocalTime.of(i,increments.get(j),0));
            }
        }

        return halfHours;
    }

    public void printDaysOfTheWeek() {
        System.out.format("%-15s","");             //15 spaces
        for(String day : this.daysOfTheWeek) {
            if(day.equals("MONDAY")) {
                System.out.format("%-18s","Mon");       //System.out.format("%-10s","Mon");
            }
            if(day.equals("TUESDAY")) {
                System.out.format("%-18s","Tue");     //System.out.format("%6s","Tue");
            }
            if(day.equals("WEDNESDAY")) {
                System.out.format("%-18s","Wed");        //System.out.format("%13s","Wed");
            }
            if(day.equals("THURSDAY")) {
                System.out.format("%-18s","Thu");        //System.out.format("%13s","Thu");
            }
            if(day.equals("FRIDAY")) {
                System.out.format("%-18s\n","Fri");        //System.out.format("%13s","Fri");
            }
        }
        System.out.println("-----------------------------------------------------------------------------------------" +
                "-----------------");
    }

    private ArrayList<LocalTime> setHoursOfTheDay() {
        ArrayList<LocalTime> hours = new ArrayList<>();
//        int latestHourInSchedule = schedule.getLatestHour();      //TODO: fix schedule == null bug

        //add hours between START_HOUR and END_HOUR, inclusive
        for(int i = START_HOUR; i <= END_HOUR; i++) {
            hours.add(LocalTime.of(i,0,0));
        }
        //if latest class exceeds 4pm, add more hours
        //This is for expanding the calendar view
//        if (latestHourInSchedule > 17) {
//            for(int i = END_HOUR; i <= latestHourInSchedule + 1; i++) {
//                hours.add(LocalTime.of(i, 0, 0));
//            }
//        }

        return hours;
    }

    /*
    public void printScheduleAsCalendar() {
        int hrCounter;                                 //acts as hour counter
        int halfHrCounter;                              //acts as 30 min counter

        printDaysOfTheWeek();

        for (LocalTime halfHr : this.halfHoursOfTheDay) {    //can replace with more granular time
            hrCounter = halfHr.getHour();
            halfHrCounter = halfHr.getMinute();

            //Print hour-blocks on left of calendar view
            if((hrCounter < 12) && (halfHrCounter == 0)) {
                System.out.printf("%2d am", hrCounter);
            } else if ((hrCounter == 12) && (halfHrCounter == 0)) {
                System.out.printf("%2d pm", hrCounter);
            } else if ((hrCounter > 12 ) && (halfHrCounter == 0)) {
                System.out.printf("%2d pm", hrCounter - 12);
            } else {
                System.out.format("%-5s","     ");
            }

            //Print classes in calendar view
            for (String day : this.daysOfTheWeek) {
                for (Course course : this.schedule.getCourses()) {
                    for (MeetingTime meetingTime : course.getMeetingTimes()) {
                        if (day.equals(meetingTime.getDay().toString())) {

                            if ((halfHr.isAfter(meetingTime.getStartTime())
                                    || halfHr.equals(meetingTime.getStartTime()))
                                    &&
                                    ((halfHr.isBefore(meetingTime.getEndTime())
                                            || halfHr.equals(meetingTime.getEndTime())))) {
                                if (halfHr.isAfter(meetingTime.getStartTime())) {
                                    System.out.format("%-15s","............");
                                } else {
                                    System.out.format("%-1s%1s %-1d%-1s", ". ",course.getDepartment(),course.getCode()," .");
//                                    System.out.format("%9s %3d", course.getDepartment(), course.getCode());

                                }
                            } else {
                                System.out.format("%-15s","            ");
                            }
                        }
                    }
                }
            }
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------------------------------------" +
                "-----------------");
    }

     */

/////////////////Working at hour-increments
    public void printScheduleAsCalendar() {
        int hrCounter;                                 //acts as hour counter
        printDaysOfTheWeek();

        for (LocalTime hour : this.hoursOfTheDay) {    //can replace with more granular time
                                                        // or while loop at smaller increment
            hrCounter = hour.getHour();
            if(hrCounter < 12) {
                System.out.printf("%2d am", hrCounter);
            } else if (hrCounter == 12) {
                System.out.printf("%2d pm", hrCounter);
            }
            else {
                System.out.printf("%2d pm", hrCounter - 12);
            }
            for (String day : this.daysOfTheWeek) {
//                System.out.format("%6s","-");   //if monday
                for (Course course : this.schedule.getCourses()) {
                    for (MeetingTime meetingTime : course.getMeetingTimes()) {
                        if (day.equals(meetingTime.getDay().toString())) {
                            int courseStartingHr = meetingTime.getStartTime().getHour();
                            int courseEndingHr = meetingTime.getEndTime().getHour();
                            if ((hrCounter >= courseStartingHr) && (hrCounter <= courseEndingHr)) {
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
