package bugbusters;

import bugbusters.Scraping.UpdatedCourses;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalTime;
import java.time.Year;
import java.util.*;

public class Registrar {
    private ArrayList<String> majors;  //may want to use list indexing if more efficient
    private ArrayList<String> minors;
    private ArrayList<Course> courses;  //we do not currently create course objects from all courses in database
    private Connection conn;
    private final int MAJOR_LIMIT = 2;   //limit to number of majors a user may have in software
    private final int MINOR_LIMIT = 4;   //limit to number of majors a user may have in software
    private final String SPRING_GRAD_MONTH = "MAY";
    private final String FALL_GRAD_MONTH = "DECEMBER";
    /**
     * Constructor for Registrar.
     * Connects to the database with given credentials and pulls major and minor names.
     * If unable to connect to database, sets lists of major and minor names with sample data.
     * @param schema
     * @param username
     * @param password
     */
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
//            disconnectFromDB();
        }
    }

    public boolean updateCourses(){

        UpdatedCourses updatedCoursesObject = new UpdatedCourses();

        if (updatedCoursesObject.status) {
            ArrayList<Pair<Course, Pair<Integer, Integer>>> updatedCourses = updatedCoursesObject.updatedCourseList;

            try {
                for (Pair pair : updatedCourses) {
                    PreparedStatement count = this.getConn().prepareStatement("" +
                            "SELECT COUNT(*) " +
                            "FROM course " +
                            "WHERE CourseName = ? AND CourseCode = ? AND Section = ? AND Semester = ? AND Year = ?;");
                    count.setString(1, ((Course) pair.getLeft()).getName());
                    count.setInt(2, ((Course) pair.getLeft()).getCode());
                    count.setString(3, String.valueOf(((Course) pair.getLeft()).getSection()));
                    count.setString(4, ((Course) pair.getLeft()).getTerm().getSeason());
                    count.setInt(5, ((Course) pair.getLeft()).getTerm().getYear());

                    ResultSet rs = count.executeQuery();

                    int numCourses = 0;
                    if (rs.next()) {
                        numCourses = rs.getInt(1);
                    }

                    if (numCourses == 0) {
                        PreparedStatement max = this.getConn().prepareStatement("Select MAX(CourseID) From course");
                        ResultSet rs1 = max.executeQuery();
                        int maxNum = 0;
                        if (rs1.next()) {
                            maxNum = rs1.getInt(1);
                        }

                        PreparedStatement ps = this.getConn().prepareStatement("INSERT INTO course VALUES " +
                                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? + 1);");
                        ps.setInt(1, ((Course) pair.getLeft()).getTerm().getYear());
                        ps.setString(2, ((Course) pair.getLeft()).getTerm().getSeason());
                        ps.setString(3, ((Course) pair.getLeft()).getDepartment());
                        ps.setInt(4, ((Course) pair.getLeft()).getCode());
                        ps.setString(5, String.valueOf(((Course) pair.getLeft()).getSection()));
                        ps.setString(6, ((Course) pair.getLeft()).getName());
                        ps.setInt(7, ((Course) pair.getLeft()).getCredits());
                        //total capacity
                        ps.setInt(8, ((int) ((Pair) pair.getRight()).getRight()));
                        //enrolled
                        ps.setInt(9, ((int) ((Pair) pair.getRight()).getRight()) - ((int) ((Pair) pair.getRight()).getLeft()));

                        String mon = "";
                        String tue = "";
                        String wed = "";
                        String thu = "";
                        String fri = "";
                        for (MeetingTime meetingTime : ((Course) pair.getLeft()).getMeetingTimes()) {
                            switch (meetingTime.getDay()) {
                                case Day.MONDAY:
                                    mon = "M";
                                    break;
                                case Day.TUESDAY:
                                    tue = "T";
                                    break;
                                case Day.WEDNESDAY:
                                    wed = "W";
                                    break;
                                case Day.THURSDAY:
                                    thu = "R";
                                    break;
                                case Day.FRIDAY:
                                    fri = "F";
                            }
                        }
                        ps.setString(10, mon);
                        ps.setString(11, tue);
                        ps.setString(12, wed);
                        ps.setString(13, thu);
                        ps.setString(14, fri);

                        if (((Course) pair.getLeft()).getMeetingTimes().size() > 0) {
                            ps.setTime(15, Time.valueOf(((Course) pair.getLeft()).getMeetingTimes().get(0).getStartTime()));
                            ps.setTime(16, Time.valueOf(((Course) pair.getLeft()).getMeetingTimes().get(0).getEndTime()));
                        } else {
                            ps.setString(15, "");
                            ps.setString(16, "");
                        }


                        if ((((Course) pair.getLeft()).getInstructor().split(" ")).length > 1) {
                            ps.setString(17, ((Course) pair.getLeft()).getInstructor().split(" ")[1]);
                        } else {
                            ps.setString(17, "");
                        }
                        ps.setString(18, ((Course) pair.getLeft()).getInstructor().split(" ")[0]);
                        ps.setString(19, "");
                        ps.setString(20, "");
                        ps.setInt(21, maxNum);

                        int rows = ps.executeUpdate();
                    } else {
                        PreparedStatement updateCapacity = this.getConn().prepareStatement("" +
                                "UPDATE course " +
                                "SET Capacity = ? " +
                                "WHERE CourseName = ? AND CourseCode = ? AND Section = ? AND Semester = ? AND Year = ?;");

                        updateCapacity.setInt(1, (int) ((Pair)pair.getRight()).getRight());
                        updateCapacity.setString(2, ((Course) pair.getLeft()).getName());
                        updateCapacity.setInt(3, ((Course) pair.getLeft()).getCode());
                        updateCapacity.setString(4, String.valueOf(((Course) pair.getLeft()).getSection()));
                        updateCapacity.setString(5, ((Course) pair.getLeft()).getTerm().getSeason());
                        updateCapacity.setInt(6, ((Course) pair.getLeft()).getTerm().getYear());

                        updateCapacity.executeUpdate();

                        PreparedStatement updateEnrolled = this.getConn().prepareStatement("" +
                                "UPDATE course " +
                                "SET Enrolled = ? " +
                                "WHERE CourseName = ? AND CourseCode = ? AND Section = ? AND Semester = ? AND Year = ?;");

                        updateCapacity.setInt(1, (int) ((Pair)pair.getRight()).getRight() - (int) ((Pair)pair.getRight()).getLeft());
                        updateCapacity.setString(2, ((Course) pair.getLeft()).getName());
                        updateCapacity.setInt(3, ((Course) pair.getLeft()).getCode());
                        updateCapacity.setString(4, String.valueOf(((Course) pair.getLeft()).getSection()));
                        updateCapacity.setString(5, ((Course) pair.getLeft()).getTerm().getSeason());
                        updateCapacity.setInt(6, ((Course) pair.getLeft()).getTerm().getYear());
                    }
                }
                System.out.println("Completely Done");
                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("Failed");
                return false;
            }
        } else {
            System.out.println("Failed to scrape");
            return false;
        }
    }


    /**
     * @return list of sample major names
     */
    private ArrayList<String> getSampleMajors() {
        ArrayList<String> sampleMajors = new ArrayList<String>();
        sampleMajors.add("B.S. in Computer Science");
        sampleMajors.add("B.S. in Business Statistics");
        sampleMajors.add("B.S. in Social Work");
        sampleMajors.add("B.S. in Biology");

        return sampleMajors;
    }

    /**
     * @return list of sample minor names
     */
    private ArrayList<String> getSampleMinors() {
        ArrayList<String> sampleMinors = new ArrayList<String>();
        sampleMinors.add("Computer Science");
        sampleMinors.add("Philosophy");
        sampleMinors.add("Cybersecurity");
        sampleMinors.add("Pre-Law");

        return sampleMinors;
    }

    /**
     * Prints Registrar's requirement years
     */
    public void printReqYears(){
        for (int req : this.getReqYrs()){
            System.out.println(req);
        }
    }

    /**
     * Connects to database with credentials
     * @param schema
     * @param username
     * @param password
     * @return
     */
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

    /**
     * Checks if registrar is connected to the database; if so, disconnects from database.
     * @return true if registrar is no longer connected to the database.
     */
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
     * Takes minor name and iterates through registrar's list of available minors.
     * @param newMinor
     * @return true if input minor name exists in registrar's list of minors
     */
    public Boolean isMinor(String newMinor){
        for (String minor : minors){
            if (minor.equalsIgnoreCase(newMinor)){
                return  true;
            }
        }
        return false;
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

    /**
     * Selects available requirement years based on current year
     * @return array of int requirement years
     */
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

    /**
     * Takes major name and iterates through registrar's list of available majors.
     * @param newMajor
     * @return true if input major name exists in registrar's list of majors
     */
    public boolean isMajor(String newMajor) {
        for(String major : majors) {
            if(major.equalsIgnoreCase(newMajor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return list of available major names
     */
    public ArrayList<String> getMajors() {
        return majors;
    }

    /**
     * @param reqYr
     * @return true if input requirement year is valid according to registrar
     */
    public boolean isReqYr(int reqYr) {
        for(int yr : getReqYrs()){
            if(reqYr == yr) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return list of available minors according to the registrar
     */
    public ArrayList<String> getMinors() {
        return minors;
    }

    /**
     * Pull all courses from database and set to Registrar's ArrayList of courses
     */
    public void setCourses() {
    }

    /**
     * @return ArrayList of Course objects from the database
     */
    public ArrayList<Course> getCourses() {
        return courses;
    }

    /**
     * Connects to database, parses course data from csv into course table in database.
     * Should only be called once for each data file to input them to course table.
     * Public only for testing purposes in RegistrarTest.java.
     * @param filename
     * @return
     */
    public int insertCoursesFromCSV(String filename) {
        int rows = 0;

        if(!connectToDB("schemaBugBuster","u222222","p222222")) {
            System.out.println("Unable to connect to database");
        } else {
            rows = parseFileWithCourses(filename);
        }

        return rows;
    }

    /**
     * Should only be called once after all data is dropped into a table in the database.
     * Public only for testing purposes in RegistrarTest.java.
     * @param tableName
     * @return
     */
    public boolean generateIdAttribute(String tableName) {
        try {
            PreparedStatement ps = conn.prepareStatement("" +
                    "ALTER TABLE " + tableName + " ADD CourseID INT(6) " +
                        "PRIMARY KEY AUTO_INCREMENT;");
            ps.execute();
            return true;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Iterates through csv and calls insertCourse() for each line.
     * Public only for testing purposes in RegistrarTest.java.
     * @param filename
     * @return updated rows in database
     */
    public int parseFileWithCourses(String filename) {
        int rows = 0;

        //Open file for reading
        //code adapted from https://howtodoinjava.com/java/io/parse-csv-files-in-java/
        try {
            Scanner scanner = new Scanner(new File(filename));

            //"Throw out" column names
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            //Read line
            while (scanner.hasNextLine()) {
                Scanner rowScanner = new Scanner(scanner.nextLine());
                rowScanner.useDelimiter(",");

                HashMap<String, Object> courseAttributes = readCourseFromCSV(rowScanner);
                rows += insertCourse(courseAttributes, filename);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return rows;
    }

    /**
     * Creates a HashMap to hold course attributes from line of data in csv file.
     * Public only for testing purposes in RegistrarTest.java.
     * @param rowScanner
     * @return
     */
    private HashMap<String, Object> readCourseFromCSV(Scanner rowScanner) {
        HashMap<String, Object> courseAttributes = new HashMap<>();

//        if (rowScanner.hasNext()) {courseAttributes.put("CourseID",rowScanner.nextInt());}
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
        if (rowScanner.hasNext()) {courseAttributes.put("PrefFNameInstructor",rowScanner.next());}
        if (rowScanner.hasNext()) {courseAttributes.put("Comments",rowScanner.next());}

//            printHashMap(courseAttributes);
        return courseAttributes;
    }

    /**
     * Inserts course as row in database.
     * Public only for testing purposes in RegistrarTest.java.
     * @param courseAttributes
     * @param filename
     * @return
     */
    private int insertCourse(HashMap<String, Object> courseAttributes,String filename) {    //will take all args;return 1 if successful
        try {
//            int courseID = (int) courseAttributes.get("CourseID");
            int year = (int) courseAttributes.get("Year");
            String semester = "";
            if (courseAttributes.get("Semester").equals("10")) {
                semester = "Spring";
            } else if (courseAttributes.get("Semester").equals("30")) {
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

            Time startTime = parseTimeAttribute(courseAttributes.get("StartTime"), filename);
            Time endTime = parseTimeAttribute(courseAttributes.get("EndTime"), filename);

            String lNameInstructor = (String) courseAttributes.get("LNameInstructor");
            String fNameInstructor = (String) courseAttributes.get("FNameInstructor");
            String prefNameInstructor = (String) courseAttributes.get("PrefFNameInstructor");
            String comment = (String) courseAttributes.get("Comments");

            PreparedStatement ps = conn.prepareStatement("" +
                    "INSERT INTO course VALUES" +
                        "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            //Pass in parameters
//            ps.setInt(1,courseID);
            ps.setInt(1,year);
            ps.setString(2,semester);
            ps.setString(3,dept);
            ps.setInt(4,code);
            ps.setString(5,section);
            ps.setString(6,courseName);
            ps.setInt(7,hours);
            ps.setInt(8,capacity);
            ps.setInt(9,enrolled);
            ps.setString(10,monday);
            ps.setString(11,tuesday);
            ps.setString(12,wednesday);
            ps.setString(13,thursday);
            ps.setString(14,friday);
            if (startTime.equals(Time.valueOf("00:00:00"))) {ps.setTime(15, null);}
            else {ps.setTime(15,startTime);}
            if (endTime.equals(Time.valueOf("00:00:00"))) {ps.setTime(16, null);}
            else {ps.setTime(16,endTime);}
            ps.setString(17,lNameInstructor);
            ps.setString(18,fNameInstructor);
            ps.setString(19,prefNameInstructor);
            ps.setString(20,comment);

            //Execute prepared statement
            int rows = ps.executeUpdate();
            return rows;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;   //insert failed; no rows updated
    }

    /**
     * Creates Time value for time-related columns.
     * Public only for testing purposes in RegistrarTest.java.
     * @param inputTime
     * @param filename
     * @return
     */
    public Time parseTimeAttribute(Object inputTime, String filename) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        String AM_PM = "AM";

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

        if ((filename == "2019-2020_GCC_Courses.csv") ||
                (filename == "2020-2021_GCC_Courses.csv")) {      //expect format "3:50:00 PM"
            Scanner scanner = new Scanner(inputTimeStr);
            scanner.useDelimiter(" ");
            if (scanner.hasNext()) {
                String time = scanner.next();
                Scanner timeScanner = new Scanner(time);
                timeScanner.useDelimiter(":");
                if (timeScanner.hasNext()) {hour = timeScanner.nextInt();}
                if (timeScanner.hasNext()) {minute = timeScanner.nextInt();}
                if (timeScanner.hasNext()) {second = timeScanner.nextInt();}
            }
            if (scanner.hasNext()) {AM_PM = scanner.next();}
            if ((AM_PM.equals("PM")) && (hour > 12)) {hour += 12;}
        }
        return Time.valueOf(LocalTime.of(hour, minute, second));
    }

    /**
     * delete row from course table in database
     * @param courseID
     * @return true if delete was successful
     */
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

    /**
     * Prints "Attribute: Value" for each attribute of a course.
     * @param courseAttributes
     */
    public void printCourseAttributes(HashMap<String, Object> courseAttributes) {
        for (String attr : courseAttributes.keySet()) {
            System.out.println(attr + ": " + courseAttributes.get(attr) + "     " +
                    courseAttributes.get(attr).getClass());
        }
    }

    /**
     * @return connection to database
     */
    public Connection getConn() {
        return conn;
    }

    // TODO: Prevent duplicate names
    /* public static int checkName(String name) {
        try {
            PreparedStatement nameCheck = conn.prepareStatement("SELECT * FROM Schedule WHERE Name = ?");
            nameCheck.setString(1, name);
            return nameCheck.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return -1;
    }*/

    /**
     * Save a schedule in the database, and generate a unique scheduleID for the entry
     * @param schedule the schedule to be saved
     * @return true if the schedule was successfully saved, and false otherwise
     */
    public boolean saveSchedule(Schedule schedule) {
        try {
            PreparedStatement checkIfExists = conn.prepareStatement("" +
                    "SELECT * FROM schedule WHERE ScheduleID = ?");
            checkIfExists.setInt(1, schedule.getScheduleID());
            ResultSet rows = checkIfExists.executeQuery();
            // if this schedule already exists, delete the old version and make a new entry for the correct version
            if (rows.next()) {
                deleteSchedule(schedule.getScheduleID());
                schedule.setScheduleID(0);
            }

            // insert into schedule first, where we save the schedule object itself
            int id = 0;

            PreparedStatement newInsertion = conn.prepareStatement("INSERT INTO schedule (UserID, Name, Year, Semester) VALUES (?,?,?,?)");
            newInsertion.setInt(1, schedule.getUserID());
            newInsertion.setString(2, schedule.getName());
            newInsertion.setInt(3, schedule.getTerm().getYear());
            newInsertion.setString(4, schedule.getTerm().getSeason());

            int addedRows = newInsertion.executeUpdate();
            newInsertion = conn.prepareStatement("SELECT LAST_INSERT_ID();");
            ResultSet results = newInsertion.executeQuery();
            while (results.next()) {
                id = results.getInt(1);
            }
            schedule.setScheduleID(id);

            // now insert into schedule_course for each course in the schedule, where we save all the course objects
            int courseRows = 1;
            for (Course course : schedule.getCourses()) {
                PreparedStatement insertScheduleEntry = conn.prepareStatement("INSERT INTO schedule_course VALUES " +
                        "(?,?,?)");
                insertScheduleEntry.setInt(1, schedule.getUserID());
                insertScheduleEntry.setInt(2, schedule.getScheduleID());
                insertScheduleEntry.setInt(3, course.getId());
                if (insertScheduleEntry.executeUpdate() == 0) {
                    courseRows = 0;
                }
            }

            // if we inserted at least one row in each table, return true
            if (addedRows > 0 && courseRows > 0) {
                //System.out.println("Schedule successfully saved with ID = " + schedule.getScheduleID());
                return true;
            }
        } catch(SQLException e) {
            System.out.println("Failed to save schedule, Exception: " + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * Deletes all entries from the schedule and schedule_course tables that match the given parameter
     * @param scheduleID the schedule to be deleted
     * @return true if at least one entry was deleted, and false otherwise
     */
    public boolean deleteSchedule(int scheduleID) {
        try {
            PreparedStatement ps1 = conn.prepareStatement("" +
                    "DELETE FROM schedule WHERE scheduleID = ?");
            ps1.setInt(1, scheduleID);

            PreparedStatement ps2 = conn.prepareStatement("" +
                    "DELETE FROM schedule_course WHERE scheduleID = ?");
            ps2.setInt(1, scheduleID);

            int rows1 = ps1.executeUpdate();
            int rows2 = ps2.executeUpdate();

            if(rows1 > 0 && rows2 > 0) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println("Failed to delete, Exception:  " + e.getMessage());
            return false;
        }
        return false;
    }

    public int loginUser(String username, String password) {
        try {
            PreparedStatement ps = conn.prepareStatement("" +
                    "SELECT userID FROM user WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int id = rs.getInt(1);
                return id;
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return -999;
    }

    public int get_MAJOR_LIMIT() {
        return MAJOR_LIMIT;
    }

    public int get_MINOR_LIMIT() {
        return MINOR_LIMIT;
    }

    public String get_SPRING_GRAD_MONTH() {
        return SPRING_GRAD_MONTH;
    }

    public String get_FALL_GRAD_MONTH() {
        return FALL_GRAD_MONTH;
    }
}
