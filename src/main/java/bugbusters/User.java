package bugbusters;

import com.mysql.cj.jdbc.jmx.LoadBalanceConnectionGroupManager;

import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class User {

    private String firstName;
    private String lastName;
    private CollegeYear collegeYear;
    private ArrayList<Major> majors;
    private ArrayList<Minor> minors;
    private Registrar registrar;
    private final int MAJOR_LIMIT = 2;   //limit to number of majors a user may have in software
    private final int MINOR_LIMIT = 4;   //limit to number of majors a user may have in software
    public User() {
        this.firstName = "";
        this.lastName = "";
        this.majors = new ArrayList<Major>();
        this.minors = new ArrayList<Minor>();
        //TODO: note that this is hard-coded for u222222
        this.registrar = new Registrar("schemaBugBuster","u222222","p222222");
        registrar.disconnectFromDB();
    }

    private void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getFirstName(){
        return firstName;
    }

    private void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getLastName(){
        return lastName;
    }

    public void setCollegeYear(CollegeYear collegeYear){
        this.collegeYear = collegeYear;
    }
    public void setCollegeYear(String collegeYearInput){
        String input = collegeYearInput.toUpperCase();
        switch (input) {
            case "FRESHMAN": this.collegeYear = CollegeYear.FRESHMAN;
                break;
            case "SOPHOMORE": this.collegeYear = CollegeYear.SOPHOMORE;
                break;
            case "JUNIOR": this.collegeYear = CollegeYear.JUNIOR;
                break;
            case "SENIOR": this.collegeYear = CollegeYear.SENIOR;
                break;
            case "SUPERSENIOR": this.collegeYear = CollegeYear.SUPERSENIOR;
                break;
        }
    }

    public CollegeYear getCollegeYear(){
        return collegeYear;
    }


    /**
     * Take name of major and requirement year and add to the user's collection of majors.
     * A user cannot add duplicate majors.
     * A user cannot add more majors than the limit.
     * @param majorName
     * @param reqYr
     */
    public void addUserMajor(String majorName, int reqYr) {
        if(majors.size() + 1 > MAJOR_LIMIT) {
            System.out.println("Cannot add more than " + MAJOR_LIMIT + " majors.");

        } else if (userHasMajor(majorName)) {
            System.out.println("Cannot add duplicate major.");

        } else if(!registrar.isMajor(majorName)) {
            System.out.println("Major '" + majorName + "' does not exist.");

        } else if (!registrar.isReqYr(reqYr)) {
            System.out.println("Majors can only belong to the following requirement years: ");
            registrar.printReqYears();
            System.out.println();
        } else {
            Major newMajor = new Major(majorName, reqYr);
            addUserMajor(newMajor);
        }
    }

    private boolean userHasMajor(String input) {
        for (Major major : majors) {
            if (major.getMajorName().equals(input)) {
                return true;
            }
        }
        return false;
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

    public ArrayList<Major> getUserMajors(){
        return majors;
    }

    /**
     * Take name of minor and requirement year and add to the user's collection of minors.
     * A user cannot add duplicate minors.
     * A user cannot add more minors than the limit.
     * @param minorName
     * @param reqYr
     */
    public void addUserMinor(String minorName, int reqYr) {
        if(minors.size() + 1 > MINOR_LIMIT) {
            System.out.println("Cannot add more than " + MINOR_LIMIT + " minors.");

        } else if (userHasMinor(minorName)) {
            System.out.println("Cannot add duplicate minor.");

        } else if(!registrar.isMinor(minorName)) {
            System.out.println("Minor '" + minorName + "' does not exist.");

        } else if (!registrar.isReqYr(reqYr)) {
            System.out.println("Minors can only belong to the following requirement years: ");
            registrar.printReqYears();
            System.out.println();
        } else {
            Minor newMinor = new Minor(minorName, reqYr);
            addUserMinor(newMinor);
        }
    }

    private boolean userHasMinor(String input) {
        for (Minor minor : minors) {
            if (minor.getMinorName().equals(input)) {
                return true;
            }
        }
        return false;
    }

    /**
     * take Minor object and add to list of user's minors
     * @param minor
     */
    private void addUserMinor(Minor minor) {
        this.minors.add(minor);
    }

    public void removeUserMinor(String minorName) {
        for(Minor minor : this.minors) {
            if(minor.getMinorName().equals(minorName)) {
                this.minors.remove(minor);
            }
        }
    }

    public List<Minor> getUserMinors(){
        return minors;
    }

}
