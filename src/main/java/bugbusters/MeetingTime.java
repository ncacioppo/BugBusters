package bugbusters;

import java.time.Duration;
import java.time.LocalTime;

public class MeetingTime implements Comparable<MeetingTime>{

//    private Day day;
    private LocalTime startTime;
    private LocalTime endTime;

    public MeetingTime(/**Day day, **/LocalTime startTime, LocalTime endTime){

    }
    public MeetingTime(String meetingTime){

    }


//    private void setDay(Day day){
//
//    }
//    public Day getDay(){
//        return null;
//    }

    private void setStartTime(LocalTime startTime){

    }
    public LocalTime getStartTime(){
        return null;
    }

    private void setEndTime(LocalTime endTime){

    }
    public LocalTime getEndTime(){
        return null;
    }


    public Duration getDuration(){
        return null;
    }


    @Override
    public boolean equals(Object other){
        return false;
    }

    @Override
    public String toString(){
        return null;
    }

    @Override
    public int compareTo(MeetingTime meetingTime){
        return 0;
    }


}
