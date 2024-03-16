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

    private final int MAJOR_LIMIT = 2;   //limit to number of majors a user may have in software
    
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
    private boolean isMajor(String newMajor) {
        for(String major : Registrar.getMajors()) {
            if(major.equals(newMajor)) {
                return true;
            }
        }
        return false;
    }

    private boolean isReasonableReqYr(int reqYr) {
        //TODO: fact check this. may need to get from db
        int endYr = Year.now().getValue();
        int startYr = Year.now().minusYears(4).getValue();
        return (reqYr < endYr) && (reqYr > startYr);
    }


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
            if(isMajor(majorName) && isReasonableReqYr(reqYr)) {
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
}
