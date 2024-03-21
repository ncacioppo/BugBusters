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
        ArrayList<String> daysOfTheWeek = new ArrayList<>(List.of("Mon","Tue","Wed","Thu","Fri"));
        ArrayList<LocalTime> hours = setHoursOfTheDay();
        this.schedule = schedule;
    }

    private void printDaysOfTheWeek() {
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

    //TODO: delete before merging
    public static void main(String[] args) {
        ArrayList<Course> springCourses = new ArrayList<Course>();

        //ACCT 201 A
        Set<MeetingTime> ACCT201A_meetingTimes = new HashSet<>();
        ACCT201A_meetingTimes.add(
                new MeetingTime(Day.MONDAY, LocalTime.of(9,0,0),
                        LocalTime.of(9,50,0)));
        ACCT201A_meetingTimes.add(
                new MeetingTime(Day.WEDNESDAY, LocalTime.of(9,0,0),
                        LocalTime.of(9,50,0)));
        ACCT201A_meetingTimes.add(
                new MeetingTime(Day.FRIDAY, LocalTime.of(9,0,0),
                        LocalTime.of(9,50,0)));

        springCourses.add(new Course(289410, "PRINCIPLES OF ACCOUNTING I", "", "ACCT",
                201, new Term("Spring", 2020), 'A', "Stone, Jennifer",
                ACCT201A_meetingTimes, 3));

        //COMP 244 B
        Set<MeetingTime> COMP244B_meetingTimes = new HashSet<>();
        COMP244B_meetingTimes.add(
                new MeetingTime(Day.TUESDAY, LocalTime.of(11,30,0),
                        LocalTime.of(12,45,0)));
        COMP244B_meetingTimes.add(
                new MeetingTime(Day.THURSDAY, LocalTime.of(11,30,0),
                        LocalTime.of(12,45,0)));

        springCourses.add(new Course(927348, "DATABASE MGT SYSTEMS", "", "COMP",
                244, new Term("Spring", 2020), 'B', "Al Moakar, Lory",
                COMP244B_meetingTimes, 3));

        //sanity check: print each course TODO:delete after testing
        for(Course c : springCourses) {
            System.out.println(c.toString());
        }

        //create new schedule
        Schedule schedule = new Schedule("Spring 2020 #1",new Term("Spring",2020),
                springCourses);

        //print schedule in calendar format
        CalendarView calendarView = new CalendarView(schedule);
        calendarView.printScheduleAsCalendar();
    }
}
