package bugbusters;

public class Minor {
    private String minorName;
    private int reqYr;

//    private List<Course> courses;

    /**
     * Constructor for Minor
     * @param minorName
     * @param reqYr
     */
    public Minor(String minorName, int reqYr){
        this.minorName = minorName;
        this.reqYr = reqYr;
    }

    /**
     * @return name of minor as String
     */
    public String getMinorName() {
        return minorName;
    }

    public int getReqYear() { return reqYr; }

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
