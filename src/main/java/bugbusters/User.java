package bugbusters;

import com.mysql.cj.jdbc.jmx.LoadBalanceConnectionGroupManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private final int userID;
    private final int MAJOR_LIMIT = 2;   //limit to number of majors a user may have in software
    
    public User() {
        this.firstName = "";
        this.lastName = "";
        this.majors = new ArrayList<Major>();
        this.minors = new ArrayList<Minor>();
        //TODO: note that this is hard-coded for u222222
        this.registrar = new Registrar("schemaBugBuster","u222222","p222222");
        this.userID = addUserToDatabase();
//        registrar.disconnectFromDB();       //TODO: all disconnects from DB should happen when user leaves app
    }

    private int addUserToDatabase() {
        int rows = 0;
        int id = 0;

        try {
            PreparedStatement ps = registrar.getConn().prepareStatement("" +
                    "INSERT INTO user() VALUES();");
            rows = ps.executeUpdate();

            ps = registrar.getConn().prepareStatement("" +
                    "SELECT LAST_INSERT_ID();");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                id = rs.getInt(1);
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
        int updatedRows = updateUserFirstName(firstName);
    }

    private int updateUserFirstName(String firstName) {
        int rows = 0;

        try {
            PreparedStatement ps = registrar.getConn().prepareStatement("" +
                    "UPDATE user SET FName = ? WHERE UserID = ?");
            ps.setString(1, firstName);
            ps.setInt(2, userID);

            rows = ps.executeUpdate();

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return rows;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
        int updatedRows = updateUserLastName(lastName);
    }
    private int updateUserLastName(String lastName) {
        int rows = 0;

        try {
            PreparedStatement ps = registrar.getConn().prepareStatement("" +
                    "UPDATE user SET LName = ? WHERE UserID = ?");
            ps.setString(1, lastName);
            ps.setInt(2, userID);

            rows = ps.executeUpdate();

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return rows;
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
    public boolean isInDatabase() {
        return userID > 0;
    }

    public int getUserID() {
        return userID;
    }

    public Registrar getRegistrar() {
        return registrar;
    }

}
