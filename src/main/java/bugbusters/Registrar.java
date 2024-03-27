package bugbusters;

import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.Properties;

public class Registrar {
    private ArrayList<String> majors;  //may want to use list indexing if more efficient
    private ArrayList<String> minors;
    private ArrayList<Course> courses;
    private int[] reqYears;
    private Connection conn;

    public Registrar(String schema, String username, String password) {
        if(!connectToDB(schema, username, password)) {
            System.out.println("Unable to connect to database");
            majors = getSampleMajors();
            minors = getSampleMinors();
            setReqYrsFromCurrent();
            //TODO: move a sample courses section in here
        } else {
            majors = new ArrayList<>();
            minors = new ArrayList<>();
            setMajorsFromDB();
            setReqYearsFromDB();
            minors = getSampleMinors();
            courses = new ArrayList<>();
//            disconnectFromDB();
        }
    }

    private void setReqYearsFromDB() {
        int[] minMaxYrs = getCourseYearsFromDB();
        try {
            setReqYrs(minMaxYrs[0],minMaxYrs[1]);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            setReqYrsFromCurrent();
        }

    }

    public int[] getCourseYearsFromDB() {
        int[] minMaxYrs = new int[2];

        try {
            PreparedStatement ps = conn.prepareStatement("" +
                    "SELECT MIN(Year) AS minYr, MAX(Year) AS maxYr FROM course");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int minYr = rs.getInt(1);
                int maxYr = rs.getInt(2);
                minMaxYrs[0] = minYr;
                minMaxYrs[1] = maxYr;
            }
        } catch(SQLException e) {
            System.out.println("Failed to get course years from database.");
            System.out.println(e.getMessage());
        }
        return minMaxYrs;
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
        sampleMinors.add("Psychology");

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
    public void setReqYrsFromCurrent() {
        int endYr = Year.now().getValue();
        int startYr = Year.now().minusYears(4).getValue();
        setReqYrs(startYr,endYr);
    }
    private void setReqYrs(int startYr, int endYr) {
        int[] reqYrs = new int[endYr-startYr+2];    //preceding year through this year

        int currYr = startYr - 1;
        for(int i = 0; i < reqYrs.length; i++) {
            reqYrs[i] = currYr;
            currYr += 1;
        }
        reqYears = reqYrs;
    }

    public boolean isMajor(String newMajor) {
        for(String major : majors) {
            if(major.equals(newMajor)) {
                return true;
            }
        }
        return false;
    }

    public boolean isMinor(String newMinor) {
        for(String minor : minors) {
            if(minor.equals(newMinor)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getMajors() {
        return majors;
    }

    public int[] getReqYrs() {return reqYears;}

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

    public void printReqYears() {
        for (int i = 0; i < reqYears.length; i++) {
            System.out.print(reqYears[i] + ", ");
        }
        System.out.print(reqYears[reqYears.length - 1]);    //print last year without following comma
    }
}
