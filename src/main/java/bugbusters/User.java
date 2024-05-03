package bugbusters;

import bugbusters.Scraping.MyGCC;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class User {

    private String firstName;
    private String lastName;
    private CollegeYear collegeYear;
    private ArrayList<Major> majors;
    private ArrayList<Minor> minors;
    private ArrayList<Schedule> schedules;
    private Registrar registrar;
    private int userID;

    /**
     * Constructor for User.
     * Creates a registrar object (i.e., a connection to the database for GCC).
     * Adds user to database with no instantiated attributes and sets userID.
     */
    public User() {
        this.firstName = "";
        this.lastName = "";
        this.majors = new ArrayList<Major>();
        this.minors = new ArrayList<Minor>();
        this.schedules = new ArrayList<>();
        this.registrar = new Registrar("schemaBugBuster","u222222","p222222");
        this.userID = addUserToDatabase();
//        registrar.disconnectFromDB();       //TODO: all disconnects from DB should happen when user leaves app
    }

    /**
     * Constructor for User who enters a username and password.
     * Creates a registrar object (i.e., a connection to the database for GCC).
     * Adds user to database with no instantiated attributes and sets userID.
     */
    public User(String username, String password) {
        this.registrar = new Registrar("schemaBugBuster","u222222","p222222");
        this.majors = new ArrayList<>();
        this.minors = new ArrayList<>();
        this.schedules = new ArrayList<>();
        this.userID = registrar.loginUser(username, password);
        if (userID == -999) {
            this.firstName = "";
            this.lastName = "";
        } else {
            setUserAttributesFromDB();
            setMajorsFromDB();
            setMinorsFromDB();
            setSchedulesFromDB();
        }
//        registrar.disconnectFromDB();       //TODO: all disconnects from DB should happen when user leaves app
    }
    public boolean importInfo(String username, String password) {
        try {
            MyGCC test = new MyGCC(username, password);
            Pair<ArrayList<Major>, ArrayList<Minor>> majorMinors = test.getInfo();
            if (majorMinors.getLeft().get(0).getMajorName().equalsIgnoreCase("Failed")){
                return false;
            } else {
                PreparedStatement deleteMajor = registrar.getConn().prepareStatement("" +
                        "DELETE FROM user_majors WHERE UserID = ?");
                deleteMajor.setInt(1, userID);
                deleteMajor.executeUpdate();
                for (Major major : majorMinors.getLeft()){
                    addUserMajor(major);
                }

                PreparedStatement deleteMinor = registrar.getConn().prepareStatement("" +
                        "DELETE FROM user_minors WHERE UserID = ?");
                deleteMinor.setInt(1, userID);
                deleteMinor.executeUpdate();
                for (Minor minor : majorMinors.getRight()){
                    addUserMinor(minor);
                }

                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void setSchedulesFromDB() {
        try {
            PreparedStatement ps = registrar.getConn().prepareStatement("" +
                    "SELECT ScheduleID, Name, Year, Semester " +
                    "FROM schedule " +
                    "WHERE UserID = ?;");
            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int scheduleID = rs.getInt(1);
                String name = rs.getString(2);
                int year = rs.getInt(3);
                String semester = rs.getString(4);
                Term term = new Term(semester, year);
                Schedule schedule = new Schedule(userID, name, term, new ArrayList<>(), new ArrayList<>());
                schedule.setCoursesFromDB(registrar.getConn());
                this.schedules.add(schedule);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setMajorsFromDB() {
        try {
            PreparedStatement ps = registrar.getConn().prepareStatement("" +
                    "SELECT m.Title, u.ReqYear " +
                            "FROM (SELECT MajorID, ReqYear " +
                                   "FROM user_majors " +
                                   "WHERE UserID = ?) AS u " +
                            "LEFT JOIN major m " +
                            "ON u.MajorID = m.MajorID;");
            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String majorName = rs.getString(1);
                int reqYr = rs.getInt(2);
                Major major = new Major(majorName, reqYr);
                this.majors.add(major);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setMinorsFromDB() {
        try {
            PreparedStatement ps = registrar.getConn().prepareStatement("" +
                    "SELECT m.Title, u.ReqYear " +
                    "FROM (SELECT MinorID, ReqYear " +
                    "FROM user_minors " +
                    "WHERE UserID = ?) AS u " +
                    "LEFT JOIN minor m " +
                    "ON u.MinorID = m.MinorID;");
            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String minorName = rs.getString(1);
                int reqYr = rs.getInt(2);
                Minor minor = new Minor(minorName, reqYr);
                this.minors.add(minor);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setUserAttributesFromDB() {
        try {
            PreparedStatement ps = registrar.getConn().prepareStatement("" +
                    "SELECT FName, LName, GradYear, GradMonth FROM user WHERE userID = ?");
            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                setFirstName(rs.getString(1));
                setLastName(rs.getString(2));

                int gradYr = rs.getInt(3);
                String gradMonth = rs.getString(4).toUpperCase();
                setCollegeYear(gradMonth, gradYr);
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
     }
    /**
     * Inserts new row for this user in database.
     * @return this user's ID (generated by MySQL)
     */
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

    private void setCollegeYear(String gradMonth, int gradYr) {
        int currYr = Year.now().getValue();
        int currMo_int = Calendar.getInstance().get(Calendar.MONTH)+1;

        int sprGradMo = Month.valueOf(registrar.get_SPRING_GRAD_MONTH()).getValue();
        int fallGradMo = Month.valueOf(registrar.get_FALL_GRAD_MONTH()).getValue();
        int gradMo = Month.valueOf(gradMonth.toUpperCase()).getValue();

        if (currMo_int <= sprGradMo) {              //If current month in Jan-May, inclusive
            if (gradMo == sprGradMo && gradYr == currYr) {
                this.collegeYear = CollegeYear.SENIOR;
            }
            else if ((gradMo == sprGradMo && gradYr == currYr + 1)
                        || (gradMo == fallGradMo && gradYr == currYr)) {
                this.collegeYear = CollegeYear.JUNIOR;
            }
            else if ((gradMo == sprGradMo && gradYr == currYr + 2)
                    || (gradMo == fallGradMo && gradYr == currYr + 1)) {
                this.collegeYear = CollegeYear.SOPHOMORE;
            }
            else if ((gradMo == sprGradMo && gradYr == currYr + 3)
                    || (gradMo == fallGradMo && gradYr == currYr + 2)) {
                this.collegeYear = CollegeYear.FRESHMAN;
            }
        } else {        //Current month is between Jun and Dec, inclusive]
            if ((gradMo == sprGradMo && gradYr == currYr + 1)
                    || (gradMo == fallGradMo && gradYr == currYr)) {
                this.collegeYear = CollegeYear.SENIOR;
            }
            else if ((gradMo == sprGradMo && gradYr == currYr + 2)
                    || (gradMo == fallGradMo && gradYr == currYr + 1)) {
                this.collegeYear = CollegeYear.JUNIOR;
            }
            else if ((gradMo == sprGradMo && gradYr == currYr + 3)
                    || (gradMo == fallGradMo && gradYr == currYr + 2)) {
                this.collegeYear = CollegeYear.SOPHOMORE;
            }
            else if ((gradMo == sprGradMo && gradYr == currYr + 4)
                    || (gradMo == fallGradMo && gradYr == currYr + 3)) {
                this.collegeYear = CollegeYear.FRESHMAN;
            }
        }
    }

    /**
     * Sets first name of both user object and user row in database.
     * @param firstName
     */
    public void setFirstName(String firstName){

        this.firstName = firstName;
        int updatedRows = updateUserFirstName(firstName);
    }

    /**
     * Updates database with first name for this user.
     * @param firstName
     * @return
     */
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

    /**
     * @return user's first name
     */
    public String getFirstName(){
        return firstName;
    }

    /**
     * Sets last name of both user object and user row in database.
     * @param lastName
     */
    public void setLastName(String lastName){
        this.lastName = lastName;
        int updatedRows = updateUserLastName(lastName);
    }

    /**
     * Updates database with last name for this user.
     * @param lastName
     * @return
     */
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

    /**
     * @return last name of user
     */
    public String getLastName(){
        return lastName;
    }

    /**
     * Take instance of CollegeYear and set user's college year
     * @param collegeYear
     */
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

    /**
     * @return user's college year
     */
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
        int majorLimit = registrar.get_MAJOR_LIMIT();
        if(majors.size() + 1 > majorLimit) {
            System.out.println("Cannot add more than " + majorLimit + " majors.");

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

    /**
     * Takes a major name and iterates through user's list of majors
     * @param input
     * @return true if user's list of majors contains input major name
     */
    private boolean userHasMajor(String input) {
        for (Major major : majors) {
            if (major.getMajorName().equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Take Major object and add to list of user's majors
     * @param major
     */
    private void addUserMajor(Major major) {
        this.majors.add(major);

        int rows = 0;
        try {
            PreparedStatement ps = registrar.getConn().prepareStatement("" +
                    "SELECT MajorID, Dept " +
                    "FROM major " +
                    "where Title = ?;");
            ps.setString(1, major.getMajorName());

            ResultSet rs1 = ps.executeQuery();
            int majorID = 0;
            String dept = "";
            if (rs1.next()) {
                majorID = rs1.getInt(1);
                dept = rs1.getString(2);
            }


            PreparedStatement addMajor = registrar.getConn().prepareStatement("" +
                    "INSERT INTO user_majors VALUES(?, ?, ?, ?)");
            addMajor.setInt(1, userID);
            addMajor.setString(2, dept);
            addMajor.setInt(3, majorID);
            addMajor.setInt(4, major.getReqYear());

            rows = addMajor.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes major from user's list of majors by name of major
     * @param majorName
     */
    public void removeUserMajor(String majorName) {
        for(Major major : this.majors) {
            if(major.getMajorName().equalsIgnoreCase(majorName)) {
                this.majors.remove(major);
                break;
            }
        }
    }

    /**
     * @return user's list of majors
     */
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
        int minorLimit = registrar.get_MINOR_LIMIT();
        if(minors.size() + 1 > minorLimit) {
            System.out.println("Cannot add more than " + minorLimit + " minors.");
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

    /**
     * Takes a minor name and iterates through user's list of minors
     * @param input
     * @return true if user's list of minors contains input minor name
     */
    private boolean userHasMinor(String input) {
        for (Minor minor : minors) {
            if (minor.getMinorName().equals(input)) {
                return true;
            }
        }
        return false;
    }

    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
        registrar.saveSchedule(schedule);
    }

    public boolean removeSchedule(Schedule toRemove) {
        for (Schedule temp : schedules) {
            if (temp.equals(toRemove)) {
                schedules.remove(toRemove);
                registrar.deleteSchedule(toRemove.getScheduleID());
                return true;
            }
        }
        return false;
    }

    public Schedule loadSchedule(String name) {
        for (Schedule temp : schedules) {
            if (temp.getName().equals(name)) {
                return temp;
            }
        }
        return null;
    }

    public Schedule loadSchedule(int id) {
        for (Schedule temp : schedules) {
            if (temp.getScheduleID() == id) {
                return temp;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        String majorString = "";
        for (Major major : majors){
            majorString += major.getMajorName() + ", ";
        }
        majorString.trim();
        //majorString = majorString.substring(0, majorString.length()-3);

        String minorString = "";
        for (Minor minor : minors){
            minorString += minor.getMinorName() + " ";
        }
        minorString = minorString.trim();
        //minorString = minorString.substring(0, minorString.length()-3);


        return (firstName + " " + lastName + " - " + collegeYear + "\n" +
                "Major(s): " + majorString + "\n" +
                "Minor(s):" + minorString);
    }

//    public static void main(String[] args) {
//        User user1 = new User();
//
//    }

    /**
     * take Minor object and add to list of user's minors
     * @param minor
     */
    private void addUserMinor(Minor minor) {
        this.minors.add(minor);

        int rows = 0;
        try {
            PreparedStatement ps = registrar.getConn().prepareStatement("" +
                    "SELECT MinorID" +
                    "FROM minor " +
                    "where Title = ?;");
            ps.setString(1, minor.getMinorName());

            ResultSet rs1 = ps.executeQuery();
            int minorID = 0;
            if (rs1.next()) {
                minorID = rs1.getInt(1);
            }


            PreparedStatement addMinor = registrar.getConn().prepareStatement("" +
                    "INSERT INTO user_minors VALUES(?, ?, ?, ?)");
            addMinor.setInt(1, userID);
            addMinor.setInt(3, minorID);
            addMinor.setInt(4, minor.getReqYear());

            rows = addMinor.executeUpdate();

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * @return true if user is in database
     */
    public boolean isInDatabase() {
        return userID > 0;
    }

    /**
     * @return user's ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Takes name of minor and removes from user's list of minors
     * @param minorName
     */
    public void removeUserMinor(String minorName) {
        for(Minor minor : this.minors) {
            if(minor.getMinorName().equalsIgnoreCase(minorName)) {
                this.minors.remove(minor);
                break;
            }
        }
    }

    /**
     * @return user's list of minors
     */
    public List<Minor> getUserMinors() {
        return minors;
    }

    /**
     * @return user's connection to the database as user's registrar
     */
    public Registrar getRegistrar() {
        return registrar;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

}
