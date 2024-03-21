package bugbusters;

import java.util.List;

public class Schedule {
    String name;
    Term term;
    List<Course> courses;

    public Schedule(String name, Term term, List<Course> courses){
        return;
    }

    private void setName(String name){
        return;
    }

    public String getName(){
        return null;
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

    public List<Course> getCourses(){
        return null;
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

    public boolean equals(Object other){
        return true;
    }

    public String toString(){
        return null;
    }

    public void printScheduleAsCalendar() {
        System.out.println(this.name);
        System.out.println();
    }
}
