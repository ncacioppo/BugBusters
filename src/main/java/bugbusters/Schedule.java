package bugbusters;

import com.mysql.cj.util.StringUtils;
import org.apache.poi.util.StringUtil;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Schedule {
    String name;
    private Term term;
    private ArrayList<Course> courses;

    public Schedule(String name, Term term, ArrayList<Course> courses){
        this.name = name;
        this.term = term;
        this.courses = courses;
    }

    private void setName(String name){
        return;
    }

    public String getName(){
        return name;
    }

    private void setTerm(Term term){
        return;
    }

    public Term getTerm(){
        return null;
    }

    private void setCourses(List<Course> courses){
        return;
    }

    public ArrayList<Course> getCourses(){
        return courses;
    }

    private boolean isValid(){
        return true;
    }

    public boolean addCourse(Course course){
        return true;
    }

    public Course removeCourse(Course course){
        return null;
    }

    public Course removeCourse(int code){
        return null;
    }

    public Course removeCourse(String courseName){
        return null;
    }

    /**
     * @return ending hour of latest course in schedule (military time)
     */
    public int getLatestHour() {
        int latestHour = 7;

        for (Course course : this.courses) {
            for (MeetingTime meetingTime : course.getMeetingTimes()) {
                int currHour = meetingTime.getEndTime().getHour();
                if (latestHour < currHour) {
                    latestHour = currHour;
                }
            }
        }
        return latestHour;
    }

    public boolean equals(Object other){
        return true;
    }

    public String toString(){
        return null;
    }
}
