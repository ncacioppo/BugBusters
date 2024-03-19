package bugbusters;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class User {

    private String firstName;
    private String lastName;
//    private CollegeYear collegeYear;
    private ArrayList<Major> majors;
    private ArrayList<Minor> minors;
    private Registrar registrar;

    private final int MAJOR_LIMIT = 2;   //limit to number of majors a user may have in software
    
    public User() {
        this.firstName = "";
        this.lastName = "";
        this.majors = new ArrayList<Major>();
        this.minors = new ArrayList<Minor>();
        this.registrar = new Registrar();
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
     * A user can add duplicate majors but will not change functionality
     * A user cannot add more majors than the limit
     * @param majorName
     * @param reqYr
     */
    public void addUserMajor(String majorName, int reqYr) {
        if(majors.size() + 1 > MAJOR_LIMIT) {
            System.out.println("Cannot add more than " + MAJOR_LIMIT + " majors.");
        } else {
            if(registrar.isMajor(majorName) && registrar.isReqYr(reqYr)) {
                Major newMajor = new Major(majorName, reqYr);
                addUserMajor(newMajor);
            }
        }
    }

    /**
     * take Major object and add to list of user's majors
     * @param major
     */
    private void addUserMajor(Major major) {
        this.majors.add(major);
    }

    public void removeUserMajor(String majorName) {
        for(Major major : this.majors) {
            if(major.getMajorName().equals(majorName)) {
                this.majors.remove(major);
            }
        }
    }

    private void setUserMajors(List<Major> majors){
    }
    public ArrayList<Major> getUserMajors(){
        return majors;
    }

    private void setMinors(List<Minor> minors){
    }

    public List<Minor> getMinors(){
        return null;
    }

    public static void main(String[] args) {
        User user1 = new User();

    }
}
