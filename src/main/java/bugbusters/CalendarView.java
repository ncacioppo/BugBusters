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

    /**
     * Sets all times on a half-hour interval that are valid class times
     * @return the list of valid times to have a class by half-hour
     */
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

    /**
     * Prints the top part of a schedule with the abbreviated days of the week in columns.
     */
    public void printDaysOfTheWeek() {
        System.out.format("%-18s","");             //15 spaces
        for(String day : this.daysOfTheWeek) {
            if(day.equals("MONDAY")) {
                System.out.format("%-19s","Mon");       //System.out.format("%-10s","Mon");
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

    /**
     * Sets all times on the hour that are valid class times
     * @return the list of valid times to have a class by hour
     */
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

    /**
     * Prints a schedule in calendar format with half-hour intervals.
     */
    public void printScheduleAsCalendar() {
        int hrCounter;                                 //acts as hour counter
        int halfHrCounter;                              //acts as 30 min counter

        printDaysOfTheWeek();

        // Print the time and any classes occurring
        // on the schedule on half-hour intervals
        for (LocalTime halfHr : this.halfHoursOfTheDay) {    //can replace with more granular time
            hrCounter = halfHr.getHour();
            halfHrCounter = halfHr.getMinute();

            //Print half-hour blocks on left of calendar view
            if (halfHrCounter == 0) { // If the time is on the hour
                if (hrCounter < 12) {
                    System.out.printf("%2d:0%d am", hrCounter, halfHrCounter);
                } else if (hrCounter == 12) {
                    System.out.printf("%2d:0%d pm", hrCounter, halfHrCounter);
                } else {
                    System.out.printf("%2d:0%d pm", hrCounter - 12, halfHrCounter);
                }
            } else { // When the time is on the half-hour
                if (hrCounter < 12) {
                    System.out.printf("%2d:%2d am", hrCounter, halfHrCounter);
                } else if (hrCounter == 12) {
                    System.out.printf("%2d:%2d pm", hrCounter, halfHrCounter);
                } else {
                    System.out.printf("%2d:%2d pm", hrCounter - 12, halfHrCounter);
                }
            }

            System.out.format("%-7s", "");

            //Print classes in calendar view
            for (String day : this.daysOfTheWeek) {
                // Boolean to track whether something has been printed
                // for the given half-hour time slot on that day
                boolean printedAlready = false;

                for (Course course : this.schedule.getCourses()) {
                    for (MeetingTime meetingTime : course.getMeetingTimes()) {
                        if (day.equals(meetingTime.getDay().toString())) { // If one of the course's meeting times falls on the current day
                            if ((halfHr.isAfter(meetingTime.getStartTime()) || halfHr.equals(meetingTime.getStartTime()))
                                    // If the time being checked is at or after the start time, and at or before the end time
                                    &&
                                    ((halfHr.isBefore(meetingTime.getEndTime()) || halfHr.equals(meetingTime.getEndTime())))) {
                                if (!printedAlready) { // If a class has not yet been printed for this time slot
                                    System.out.format("%1s %-1d%-10s", course.getDepartment(),course.getCode(), "");
                                    printedAlready = true;
                                }
                            }
                        }
                    }
                }

                // If no class has been printed for this time
                // slot on the given day, there is no class then
                if (!printedAlready) {
                    System.out.format("%-19s","");
                }
            }
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------------------------------------" +
                "-----------------");
    }

    // Alternate method to print a schedule with only hour-long intervals
/////////////////Working at hour-increments

//    public void printScheduleAsCalendar() {
//        int hrCounter;                                 //acts as hour counter
//        printDaysOfTheWeek();
//
//        for (LocalTime hour : this.hoursOfTheDay) {    //can replace with more granular time
//                                                        // or while loop at smaller increment
//            hrCounter = hour.getHour();
//            if(hrCounter < 12) {
//                System.out.printf("%2d am", hrCounter);
//            } else if (hrCounter == 12) {
//                System.out.printf("%2d pm", hrCounter);
//            }
//            else {
//                System.out.printf("%2d pm", hrCounter - 12);
//            }
//            for (String day : this.daysOfTheWeek) {
////                System.out.format("%6s","-");   //if monday
//                for (Course course : this.schedule.getCourses()) {
//                    for (MeetingTime meetingTime : course.getMeetingTimes()) {
//                        if (day.equals(meetingTime.getDay().toString())) {
//                            int courseStartingHr = meetingTime.getStartTime().getHour();
//                            int courseEndingHr = meetingTime.getEndTime().getHour();
//                            if ((hrCounter >= courseStartingHr) && (hrCounter <= courseEndingHr)) {
//                                System.out.format("%9s %3d",course.getDepartment(),course.getCode());
//                            } else {
//                                System.out.format("%9s %3s","    ", "   ");
//                            }
//                        }
//                    }
//                }
//            }
//            System.out.println();
//        }
//    }
}