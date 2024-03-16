package bugbusters;

import java.sql.*;
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
        majors = new ArrayList<String>();

        return majors;
    }

    public static ArrayList<String> getMinors() {
        return minors;
    }

    public static ArrayList<Course> getCourses() {
        return courses;
    }

    public static void main(String[] args) {
        System.out.println(connectToDB("schemaBugBuster","u222222","p222222"));
        System.out.println(disconnectFromDB());
    }
}
