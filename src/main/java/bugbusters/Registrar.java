package bugbusters;

import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class Registrar {
    private static Set<String> majors;  //may want to use list indexing is more efficient
    private static Set<String> minors;
    private static List<Course> courses;
    private static Connection conn;

    //TODO:we can make a Registrar constructor (+remove 'static' throughout) if for multiple universities

    private static boolean connectToDB() {
        try {
            //Get a properties variable so that we can pass the username and password to
            // the database.
            Properties info = new Properties();

            //Set the username and password
            info.put("user", "u222222");        //User: Andrea
            info.put("password", "p222222");    //User: Andrea

            //Connect to the database (schemabugbuster)
            conn = DriverManager.getConnection("jdbc:mysql://CSDB1901/" + "schemaBugBuster", info);
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
    public static Set<String> getMajors() {
        if(!connectToDB()) {
            System.out.println("Unable to connect to database");
            return null;
        }

        return majors;
    }

    public static Set<String> getMinors() {
        return minors;
    }

    public static List<Course> getCourses() {
        return courses;
    }

    public static void main(String[] args) {
        System.out.println(connectToDB());
        System.out.println(disconnectFromDB());
    }
}
