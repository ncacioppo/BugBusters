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
        //TODO: implement method
    }

    public static void main(String[] args) {
        System.out.println(connectToDB("schemaBugBuster","u222222","p222222"));
        System.out.println(disconnectFromDB());
//        insertCourses(filepath);
//        insertCourses(filepath);
//        insertCourses(filepath);
    }
}
