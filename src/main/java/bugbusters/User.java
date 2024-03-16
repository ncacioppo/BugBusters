package bugbusters;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String firstName;
    private String lastName;
//    private CollegeYear collegeYear;
    private ArrayList<Major> majors;
    private ArrayList<Minor> minors;

    public User() {
        this.firstName = "";
        this.lastName = "";
        this.majors = new ArrayList<Major>();
        this.minors = new ArrayList<Minor>();
    }

//    public User(String firstName, String lastName, /**CollegeYear collegeYear,**/ List<Major> majors, List<Minor> minors){
//        // change again for in-class demonstration
//    }

    private void setFirstName(String firstName){

    }
    public String getFirstName(){
        return null;
    }

    private void setLastName(String lastName){

    }
    public String getLastName(){
        return null;
    }

//    private void setCollegeYear(CollegeYear collegeYear){
//
//    }
//    public CollegeYear getCollegeYear(){
//        return null;
//    }

    /**
     * Take name of major and requirements year and add to the user's collection of majors
     * @param majorName
     * @param reqYr
     */
    public void addUserMajor(String majorName, int reqYr) {
        Major newMajor = new Major(majorName, reqYr);
        addUserMajor(newMajor);
    }

    /**
     * take Major object and add to list of user's majors
     * @param major
     */
    private void addUserMajor(Major major) {
        this.majors.add(major);
    }

    private void setMajors(List<Major> majors){
    }
    public List<Major> getMajors(){
        return majors;
    }

    private void setMinors(List<Minor> minors){
    }

    public List<Minor> getMinors(){
        return null;
    }
}
