package bugbusters;

import java.util.List;

public class Major {
    private String majorName;
    private int reqYr;

//    private List<Course> courses;

    /**
     * Constructor for Major
     * @param majorName
     * @param reqYr
     */
    public Major(String majorName, int reqYr){
        this.majorName = majorName;
        this.reqYr = reqYr;
    }

    /**
     * @return name of major as string
     */
    public String getMajorName() {
        return majorName;
    }
//
//
//    private void setCourses(List<Course> courses){
//
//    }
//    public List<Course> getCourses(){
//        return null;
//    }
//
//
//    public boolean addCourse(Course course){
//        return false;
//    }
//
//    public Course removeCourse(Course course){
//        return null;
//    }

}
