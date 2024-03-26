package bugbusters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InvalidClassException;
import java.sql.*;
import java.time.LocalTime;
import java.time.Year;
import java.util.*;

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

    public int insertCoursesFromCSV(String filename) {
        //TODO: implement method; rows var for testing purposes
        int rows = 0;

        if(!connectToDB("schemaBugBuster","u222222","p222222")) {
            System.out.println("Unable to connect to database");
        } else {
            HashMap<String, Object> courseAttributes = readCoursesFromCSV(filename);
            rows = insertCourse(courseAttributes,filename);
        }
        return rows;
    }

    public boolean deleteCourse(int courseID) {
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

    public void printHashMap(HashMap<String, Object> courseAttributes) {
        for (String attr : courseAttributes.keySet()) {
            System.out.println(attr + ": " + courseAttributes.get(attr) + "     " +
                    courseAttributes.get(attr).getClass());
        }
    }

    private HashMap<String, Object> readCoursesFromCSV(String filename) {
        HashMap<String, Object> courseAttributes = new HashMap<>();

        //Open file for reading
        //code adapted from https://howtodoinjava.com/java/io/parse-csv-files-in-java/
        try {
            Scanner scanner = new Scanner(new File(filename));

            //"Throw out" column names
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            //Read line
//            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                Scanner rowScanner = new Scanner(line);
                rowScanner.useDelimiter(",");

            if (rowScanner.hasNext()) {courseAttributes.put("CourseID",rowScanner.nextInt());}
            if (rowScanner.hasNext()) {courseAttributes.put("Year",rowScanner.nextInt());}
            if (rowScanner.hasNext()) {courseAttributes.put("Semester",rowScanner.next());}
            if (rowScanner.hasNext()) {courseAttributes.put("Dept",rowScanner.next());}
            if (rowScanner.hasNext()) {courseAttributes.put("CourseCode",rowScanner.nextInt());}
            if (rowScanner.hasNext()) {courseAttributes.put("Section",rowScanner.next());}
            if (rowScanner.hasNext()) {courseAttributes.put("CourseName",rowScanner.next());}
            if (rowScanner.hasNext()) {courseAttributes.put("Hours",rowScanner.nextInt());}
            if (rowScanner.hasNext()) {courseAttributes.put("Capacity",rowScanner.nextInt());}
            if (rowScanner.hasNext()) {courseAttributes.put("Enrolled",rowScanner.nextInt());}
            if (rowScanner.hasNext()) {courseAttributes.put("Monday",rowScanner.next());}
            if (rowScanner.hasNext()) {courseAttributes.put("Tuesday",rowScanner.next());}
            if (rowScanner.hasNext()) {courseAttributes.put("Wednesday",rowScanner.next());}
            if (rowScanner.hasNext()) {courseAttributes.put("Thursday",rowScanner.next());}
            if (rowScanner.hasNext()) {courseAttributes.put("Friday",rowScanner.next());}
            if (rowScanner.hasNext()) {courseAttributes.put("StartTime",rowScanner.next());}
            if (rowScanner.hasNext()) {courseAttributes.put("EndTime",rowScanner.next());}
            if (rowScanner.hasNext()) {courseAttributes.put("LNameInstructor",rowScanner.next());}
            if (rowScanner.hasNext()) {courseAttributes.put("FNameInstructor",rowScanner.next());}
            if (rowScanner.hasNext()) {courseAttributes.put("PreferredFNameInstructor",rowScanner.next());}
            if (rowScanner.hasNext()) {courseAttributes.put("Comments",rowScanner.next());}

            printHashMap(courseAttributes);
//            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return courseAttributes;
    }

    private int insertCourse(HashMap<String, Object> courseAttributes,String filename) {    //will take all args;return 1 if successful
        //TODO: parse attributes
        int courseID = (int) courseAttributes.get("CourseID");   //TODO: generate courseID
        int year = (int) courseAttributes.get("Year");
        String semester = "";
        if (courseAttributes.get("Semester") == "10") {
            semester = "Spring";
        } else if (courseAttributes.get("Semester") == "30") {
            semester = "Fall";
        }
        String dept = (String) courseAttributes.get("Dept");
        int code = (int) courseAttributes.get("CourseCode");
        String section = (String) courseAttributes.get("Section");
        String courseName = (String) courseAttributes.get("CourseName");
        int hours = (int) courseAttributes.get("Hours");
        int capacity = (int) courseAttributes.get("Capacity");
        int enrolled = (int) courseAttributes.get("Enrolled");
        String monday = (String) courseAttributes.get("Monday");
        String tuesday = (String) courseAttributes.get("Tuesday");
        String wednesday = (String) courseAttributes.get("Wednesday");
        String thursday = (String) courseAttributes.get("Thursday");
        String friday = (String) courseAttributes.get("Friday");

        Time startTime = parseTimeAttribute(courseAttributes.get("StartTime"),filename);
        Time endTime = parseTimeAttribute(courseAttributes.get("EndTime"),filename);

        String lNameInstructor = (String) courseAttributes.get("LNameInstructor");
        String fNameInstructor = (String) courseAttributes.get("FNameInstructor");
        String prefNameInstructor = (String) courseAttributes.get("PreferredFNameInstructor");
        String comment = (String) courseAttributes.get("Comments");


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
            if (startTime.equals(Time.valueOf("00:00:00"))) {ps.setTime(16, null);}
            else {ps.setTime(16,startTime);}
            if (endTime.equals(Time.valueOf("00:00:00"))) {ps.setTime(17, null);}
            else {ps.setTime(17,endTime);}
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

    public Time parseTimeAttribute(Object inputTime, String filename) {
        int hour = 0;
        int minute = 0;
        int second = 0;

        String inputTimeStr = (String) inputTime;

        if (filename == "2018-2019_GCC_Courses.csv") {      //expect format "1/1/1900 16:00"
            Scanner scanner = new Scanner(inputTimeStr);
            scanner.useDelimiter(" ");
            if (scanner.hasNext()) {String date = scanner.next();}
            if (scanner.hasNext()) {
                String time = scanner.next();
                Scanner timeScanner = new Scanner(time);
                timeScanner.useDelimiter(":");
                if (timeScanner.hasNext()) {hour = timeScanner.nextInt();}
                if (timeScanner.hasNext()) {minute = timeScanner.nextInt();}
                return Time.valueOf(LocalTime.of(hour, minute, second));
            }
        }
        return Time.valueOf(LocalTime.of(hour, minute, second));
    }

}
