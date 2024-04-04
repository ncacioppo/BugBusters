package bugbusters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Schedule {
    String name;
    Term term;
    List<Course> courses;
    int scheduleID;
    int userID;

    public Schedule(String name, Term term, List<Course> courses){
        setName(name);
        setTerm(term);
        setCourses(courses);
        //setUserID(user.getID())
    }

    public Schedule(Schedule schedule) {
        setName(schedule.getName());
        setTerm(schedule.getTerm());
        setCourses(schedule.getCourses());
        setUserID(schedule.getUserID());
        setScheduleID(schedule.getScheduleID());
    }

    private void setName(String name) {this.name = name; }

    public String getName(){
        return name;
    }

    private void setTerm(Term term){
        this.term = term;
    }

    public Term getTerm(){
        return term;
    }

    private void setCourses(List<Course> courses){
        this.courses = new ArrayList<>(courses);
    }

    public List<Course> getCourses(){
        return courses;
    }

    private void setUserID(int userID) {
        this.userID = 0;
   }

    public int getUserID() {
        return userID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public boolean isValid(){
        // loop through our courses
        for (Course course1 : courses) {
            ArrayList<MeetingTime> course1Times = course1.getMeetingTimes();
            // loop through every course for every course
            for (Course course2 : courses) {
                // if the courses are different
                if (!course1.equals(course2)) {
                    ArrayList<MeetingTime> course2Times = course2.getMeetingTimes();
                    // for the meeting times of course 1
                    for (MeetingTime course1MT : course1Times) {
                        // compare to the meeting times of course 2
                        for (MeetingTime course2MT : course2Times) {
                            // if the course times overlap return false
                            if (course1MT.getStartTime().isBefore(course2MT.getEndTime())
                                    && course1MT.getEndTime().isAfter(course2MT.getStartTime())) {
                                return false;
                            }
                        }
                    }
                }
                // if course1 and course2 are two sections of the same class
                if (course1.getName().equals(course2.getName())
                && course1.getSection() != course2.getSection()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean addCourse(Course course){
        if (course != null) {
            Schedule scheduleCopy = new Schedule(this);
            // if our schedule doesn't already contain this course
            if (!scheduleCopy.getCourses().contains(course))  {
                scheduleCopy.getCourses().add(course);
                // if our resulting schedule is valid, add it to the real schedule
                if (scheduleCopy.isValid()) {
                    this.courses.add(course);
                    return true;
                }
            }
        }
        return false;
    }

    public Course removeCourse(Course course){
        Course removed;
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).equals(course)) {
                removed = courses.get(i);
                courses.remove(i);
                return removed;
            }
        }
        return null;
    }

    public Course removeCourse(int code){
        Course removed;
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCode() == code) {
                removed = courses.get(i);
                courses.remove(i);
                return removed;
            }
        }
        return null;
    }

    public Course removeCourse(String courseName){
        Course removed;
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getName().equals(courseName)) {
                removed = courses.get(i);
                courses.remove(i);
                return removed;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object other){
        if (!(other instanceof Schedule schedule)) {
            return false;
        }
        if (!name.equals((schedule.name))) {
            return false;
        } else if (!(term.equals(schedule.term))) {
            return  false;
        } else return courses.equals(schedule.courses);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n").append(term).append("\n").append("\n");
        for (Course course : courses) {
            sb.append(course.shortToString()).append("\n").append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    public static String compareView(Schedule scheduleA, Schedule scheduleB) {
        StringBuilder sb = new StringBuilder();
        String formatString = "%-40s %-40s\n";
        sb.append(String.format(formatString, scheduleA.getName(), scheduleB.getName()));
        sb.append(String.format(formatString, scheduleA.getTerm(), scheduleB.getTerm()));
        int loopLength = scheduleA.getCourses().size();
        if (scheduleB.getCourses().size() > loopLength) {
            loopLength = scheduleB.getCourses().size();
        }
        sb.append("\n");
        for (int i = 0; i < loopLength; i++) {
            sb.append(String.format(formatString,
                    scheduleA.getCourses().get(i).getDepartment() + " " + scheduleA.getCourses().get(i).getCode() + " " + scheduleA.getCourses().get(i).getSection(),
                    scheduleB.getCourses().get(i).getDepartment() + " " + scheduleB.getCourses().get(i).getCode() + " " + scheduleB.getCourses().get(i).getSection()));
            sb.append(String.format(formatString,
                    scheduleA.getCourses().get(i).getName(),
                    scheduleB.getCourses().get(i).getName()
                    ));
            sb.append(String.format(formatString,
                    scheduleA.getCourses().get(i).meetingTimesToString(),
                    scheduleB.getCourses().get(i).meetingTimesToString()
                    ));
            sb.append("\n");
        }
        return sb.toString();
    }
}
