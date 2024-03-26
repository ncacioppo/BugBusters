package bugbusters;

import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Registrar {
    private ArrayList<String> majors;  //may want to use list indexing if more efficient
    private ArrayList<String> minors;
    private ArrayList<Course> courses;
    private Connection conn;

    public Registrar(String schema, String username, String password) {
        if(!connectToDB(schema, username, password)) {
            System.out.println("Unable to connect to database");
            majors = getSampleMajors();
            minors = getSampleMinors();
            //TODO: move a sample courses section in here
        } else {
            majors = new ArrayList<>();
            minors = new ArrayList<>();
            setMajorsFromDB();
            setMinorsFromDB();
            courses = new ArrayList<>();
        }
    }

    private ArrayList<String> getSampleMajors() {
        ArrayList<String> sampleMajors = new ArrayList<String>();
        sampleMajors.add("B.S. in Computer Science");
        sampleMajors.add("B.S. in Business Statistics");
        sampleMajors.add("B.S. in Social Work");
        sampleMajors.add("B.S. in Biology");

        return sampleMajors;
    }

    private ArrayList<String> getSampleMinors() {
        ArrayList<String> sampleMinors = new ArrayList<String>();
        sampleMinors.add("Computer Science");
        sampleMinors.add("Philosophy");
        sampleMinors.add("Cybersecurity");
        sampleMinors.add("Pre-Law");

        return sampleMinors;
    }

    private boolean connectToDB(String schema, String username, String password) {
        try {
            //Get a properties variable so that we can pass the username and password to
            // the database.
            Properties info = new Properties();

            //Set the username and password
            info.put("user", username);
            info.put("password", password);

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

    public boolean disconnectFromDB() {
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
     * Calls connectToDB() and pulls major titles into an ArrayList
     * B.S. in Computer Science, B.A. in Computer Science, or B.S. in Data Science
     * combines ex. "B.S." + " in " + "Computer Science"
     * @return set of major names
     */
    private void setMajorsFromDB() {
        try {
            PreparedStatement ps = conn.prepareStatement("" +
                    "SELECT ArtSci, Title FROM major");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String artsci = rs.getString(1);
                String title = rs.getString(2);
                String output;

                if (artsci == null) {
                    output = title;
                } else if (artsci.equals("Arts")) {
                    output = "B.A. in " + title;
                } else {
                    output = "B.S. in " + title;
                }
                majors.add(output);
            }

        } catch(SQLException e) {
            System.out.println("Failed to select majors from database.");
            System.out.println(e.getMessage());
        }
    }


    /**
     * Calls connectToDB() and pulls minor titles into an ArrayList
     * @return set of major names
     */
    private void setMinorsFromDB() {
        try {
            PreparedStatement ps = conn.prepareStatement("" +
                    "SELECT Title FROM minor");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String title = rs.getString(1);
                minors.add(title);
            }

        } catch(SQLException e) {
            System.out.println("Failed to select minors from database.");
            System.out.println(e.getMessage());
        }
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
        for(String major : majors) {
            if(major.equals(newMajor)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getMajors() {
        return majors;
    }

    public boolean isReqYr(int reqYr) {
        for(int yr : getReqYrs()){
            if(reqYr == yr) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getMinors() {
        return minors;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public static void main(String[] args) {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        System.out.println(registrar.disconnectFromDB());
    }
}
