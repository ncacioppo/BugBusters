package bugbusters;

import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Registrar {
    private static ArrayList<String> majors;  //may want to use list indexing if more efficient
    private static ArrayList<String> minors;
    private static ArrayList<Course> courses;
    private static Connection conn;

    public Registrar() {
        majors = new ArrayList<>();
        minors = new ArrayList<>();
        courses = new ArrayList<>();
        conn = null;
    }

    private static boolean connectToDB(String schema, String username, String password) {
        try {
            //Get a properties variable so that we can pass the username and password to
            // the database.
            Properties info = new Properties();

            //Set the username and password
            info.put("user", username);
            info.put("password", password);


            //Connect to the database (schemabugbuster)
            conn = DriverManager.getConnection("jdbc:mysql://CSDB1901/" + "schemaBugBuster", info);
//            conn = DriverManager.getConnection("jdbc:mysql://CSDB1901/" + schema, info);
            //TODO: delete this print statement after testing
            System.out.println("Connection successful");
        } catch(SQLException e) {
            //TODO: delete this print statement after testing
            System.out.println("Connection unsuccessful");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private static boolean disconnectFromDB() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch(SQLException e) {
            //TODO: delete this print statement after testing
            System.out.println("Unable to disconnect from database");
            System.out.println(e.getMessage());
            return false;
        }
        //TODO: delete this print statement after testing
        System.out.println("Successfully disconnected");
        return true;
    }

    /**
     * Calls connectToDB() and pulls major titles into a set
     * B.S. in Computer Science, B.A. in Computer Science, or B.S. in Data Science
     * combines ex. "B.S." + " in " + "Computer Science"
     * @return set of major names
     */
    public static ArrayList<String> getMajors() {
//        if(!connectToDB()) {
//            System.out.println("Unable to connect to database");
//            return null;
//        }
        //TODO: delete below after testing
        majors = new ArrayList<String>();
        majors.add("B.S. in Computer Science");
        majors.add("B.S. in Business Statistics");
        majors.add("B.S. in Social Work");
        majors.add("B.S. in Biology");

        return majors;
    }
    public int[] getReqYrs() {
        //TODO: factcheck this. may need to get from db
        int endYr = Year.now().getValue();
        int startYr = Year.now().minusYears(4).getValue();
        int[] reqYrs = new int[endYr-startYr];

        int currYr = startYr;
        for(int i = 0; i < reqYrs.length; i++) {
            reqYrs[i] = currYr;
            currYr += 1;
        }
        return reqYrs;
    }

    public boolean isMajor(String newMajor) {
        for(String major : Registrar.getMajors()) {
            if(major.equals(newMajor)) {
                return true;
            }
        }
        return false;
    }

    public boolean isReqYr(int reqYr) {
        for(int yr : getReqYrs()){
            if(reqYr == yr) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getMinors() {
        return minors;
    }

    public static ArrayList<Course> getCourses() {
        return courses;
    }

    private static void insertCourses(String filepath) {
        int rows = 0;

        //read values from csv and insert as args below
        rows += insertCourse();
    }

    private static boolean deleteCourse(int courseID) {
        try {
            PreparedStatement ps = conn.prepareStatement("" +
                    "DELETE FROM course WHERE courseID = ?");
            ps.setInt(1, courseID);

            int rows = ps.executeUpdate();
            if(rows > 0) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println("Failed to delete exception is " + e.getMessage());
            return false;
        }
        return false;
    }

    private static int insertCourse() {    //will take all args;return 1 if successful
        //dummy data, assume type casting
        int courseID = 289410;   //TODO: we need to scrape refNums or create sql function
        int year = 2020;
        String semester = "Spring";
        String dept = "ACCT";
        int code = 201;
        String section = "A";
        String courseName = "PRINCIPLES OF ACCOUNTING I";
        int hours = 3;
        int capacity = 30;
        int enrolled = 30;
        String monday = "M";
        String tuesday = "";
        String wednesday = "W";
        String thursday = "";
        String friday = "F";
        Time startTime = new Time(9,0,0);
        Time endTime = new Time(9,50,0);
        String lNameInstructor = "Stone";
        String fNameInstructor = "Jennifer";
        String prefNameInstructor = "Nicole";
        String comment = "Online materials fee";
        ////

        try{
            //TODO: remove hard-coded username and password
            connectToDB("schemaBugBuster", "u222222", "p222222");
            PreparedStatement ps = conn.prepareStatement("" +
                    "INSERT INTO course VALUES" +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");    //TODO: ADD ????

            //Pass in parameters
            ps.setInt(1,courseID);
            ps.setInt(2,year);
            ps.setString(3,semester);
            ps.setString(4,dept);
            ps.setInt(5,code);
            ps.setString(6,section);
            ps.setString(7,courseName);
            ps.setInt(8,hours);
            ps.setInt(9,capacity);
            ps.setInt(10,enrolled);
            ps.setString(11,monday);
            ps.setString(12,tuesday);
            ps.setString(13,wednesday);
            ps.setString(14,thursday);
            ps.setString(15,friday);
            ps.setTime(16,startTime);
            ps.setTime(17,endTime);
            ps.setString(18,lNameInstructor);
            ps.setString(19,fNameInstructor);
            ps.setString(20,prefNameInstructor);
            ps.setString(21,comment);

            //Execute prepared statement
            //TODO: adjust for replication
            int rows = ps.executeUpdate();
            disconnectFromDB();
            return rows;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;   //insert failed; no rows updated
    }

    public static void main(String[] args) {
        System.out.println(connectToDB("schemaBugBuster","u222222","p222222"));
        System.out.println(deleteCourse(289410));
        System.out.println(Registrar.insertCourse());
        System.out.println(disconnectFromDB());
//        insertCourses(filepath);
//        insertCourses(filepath);
//        insertCourses(filepath);
    }
}
