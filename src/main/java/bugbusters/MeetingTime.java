package bugbusters;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Scanner;

public class MeetingTime implements Comparable<MeetingTime>{

    private Day day;
    private LocalTime startTime;
    private LocalTime endTime;

    public MeetingTime(Day day, LocalTime startTime, LocalTime endTime){
        setDay(day);
        setStartTime(startTime);
        setEndTime(endTime);
    }
    public MeetingTime(String meetingTime){
        Scanner scn = new Scanner(meetingTime);
        setDay(Day.valueOf(scn.next()));
        setStartTime(LocalTime.parse(scn.next()));
        scn.useDelimiter(" to ");
        setEndTime(LocalTime.parse(scn.next()));
    }

    public MeetingTime(MeetingTime meetingTime) {
        setDay(meetingTime.getDay());
        setStartTime(meetingTime.getStartTime());
        setEndTime(meetingTime.getEndTime());
    }


    private void setDay(Day day){
        this.day = day;
    }
    public Day getDay(){
        return day;
    }

    private void setStartTime(LocalTime startTime){
        this.startTime = startTime;
    }
    public LocalTime getStartTime(){
        return startTime;
    }

    private void setEndTime(LocalTime endTime){
        this.endTime = endTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }


    public Duration getDuration(){
        return Duration.between(startTime, endTime);
    }


    @Override
    public boolean equals(Object other){
        if (!(other instanceof MeetingTime mt)) {
            return false;
        }
        if (!(day.equals(mt.day))) {
            return false;
        } else if (!(startTime.equals(mt.startTime))) {
            return false;
        } else return endTime.equals((mt.endTime));
    }

    // Format: MONDAY 10:00 to 10:50
    @Override
    public String toString(){
        return getDay().toString() + " " + getStartTime().toString() + " to " + getEndTime().toString();
    }

    @Override
    public int compareTo(MeetingTime meetingTime){
        if (this.equals(meetingTime)) {
            return 0;
        }
        if ((this.day.compareTo(meetingTime.getDay()) < 1) && (this.startTime.isBefore(meetingTime.getStartTime()))) {
            return -1;
        }
        return 1;
    }


}
