package bugbusters;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseSearch {
    private Connection conn;
    private StringBuilder query;   //prepared statement for querying courses
    private ArrayList<SearchFilter> filters;
    private ArrayList<Course> results;

    /**
     * Constructor for DatabaseSearch
     * @param conn from Registrar instance
     */
    public DatabaseSearch(Connection conn) {
        this.conn = conn;
        this.query = new StringBuilder("SELECT * FROM course");
        this.filters = new ArrayList<>();
    }

    public void addFilter(Filter filter, String userQuery) {
        filters.add(new SearchFilter(filter, userQuery));
    }
    public PreparedStatement refineQuery() {
        query.append(" WHERE ");
        for(int i = 0; i < filters.size(); i++) {
            query.append(filters.get(i).getClause());
            if(i != filters.size() - 1) {
                query.append(" AND ");
            }
        }
        query.append(";");

        try {
            PreparedStatement ps = conn.prepareStatement(String.valueOf(query));
            int i = 1;

            for(SearchFilter filter : filters) {
                switch(filter.getType()) {
                    case Filter.DEPARTMENT:
                        ps.setString(i, filter.getKey());
                        break;
                    case Filter.ID:
                        ps.setInt(i, scanKeyInt(filter.getKey()));
                        break;
                    case Filter.NAME:
                        String key = "%" + filter.getKey() + "%";
                        ps.setString(i, key);
                        break;
                }
                i += 1;
            }
            return ps;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private int scanKeyInt(String key) {
        Scanner scanner = new Scanner(key);
        while(scanner.hasNextInt()) {
            return scanner.nextInt();
        }

        return -999;
    }

    /**
     * Executes this DatabaseSearch's query
     * @return ArrayList of results as course objects
     */
    public ArrayList<Course> executeQuery() {
        PreparedStatement ps;

        try {
            if(!filters.isEmpty()) {
                ps = refineQuery();
            } else {
                ps = conn.prepareStatement(String.valueOf(query));
            }

            ResultSet rs = ps.executeQuery();
            results = getCourseResults(rs);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return results;
    }

    /**
     * Parse ResultSet from query execution into Course objects
     * @param rs
     * @return ArrayList of results as Course objects
     */
    private ArrayList<Course> getCourseResults(ResultSet rs) {
        results = new ArrayList<>();

        try {
            while (rs.next()) {
                int year = rs.getInt(1);
                String semester = rs.getString(2);
                String dept = rs.getString(3);
                int courseCode = rs.getInt(4);
                char section = rs.getString(5).charAt(0);
                String courseName = rs.getString(6);
                int hours = rs.getInt(7);
                int capacity = rs.getInt(8);
                int enrolled = rs.getInt(9);
                String monday = rs.getString(10);
                String tuesday = rs.getString(11);
                String wednesday = rs.getString(12);
                String thursday = rs.getString(13);
                String friday = rs.getString(14);

                LocalTime startTime = null;
                Time start = rs.getTime(15);
                if(start != null) {
                    startTime = start.toLocalTime();
                }
                LocalTime endTime = null;
                Time end = rs.getTime(16);
                if(end != null) {
                    endTime = end.toLocalTime();
                }

                String lNameInstructor = rs.getString(17);
                String fNameInstructor = rs.getString(18);
                String prefNameInstructor = rs.getString(19);
                String comments = rs.getString(20);
                int id = rs.getInt(21);

                ArrayList<MeetingTime> meetingTimes = setMeetingTimes(monday, tuesday,
                        wednesday, thursday, friday, startTime, endTime);
                Term term = new Term(semester, year);
                String instructor = fNameInstructor + " " + prefNameInstructor +
                        " " + lNameInstructor;

                Course course = new Course(id,courseName,"",dept,courseCode,term,section,
                        instructor,meetingTimes,hours);
                results.add(course);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return results;
    }

    /**
     * Create a list of meeting times for a course
     * @param monday
     * @param tuesday
     * @param wednesday
     * @param thursday
     * @param friday
     * @param startTime
     * @param endTime
     * @return ArrayList of MeetingTime objects
     */
    private ArrayList<MeetingTime> setMeetingTimes(String monday, String tuesday, String wednesday, String thursday, String friday, LocalTime startTime, LocalTime endTime) {
        ArrayList<MeetingTime> meetingTimes = new ArrayList<>();

        if(monday.equals("M")) {
            meetingTimes.add(new MeetingTime(Day.MONDAY, startTime, endTime));
        }
        if(tuesday.equals("T")) {
            meetingTimes.add(new MeetingTime(Day.TUESDAY, startTime, endTime));
        }
        if(wednesday.equals("W")) {
            meetingTimes.add(new MeetingTime(Day.WEDNESDAY, startTime, endTime));
        }
        if(thursday.equals("R")) {
            meetingTimes.add(new MeetingTime(Day.THURSDAY, startTime, endTime));
        }
        if(friday.equals("F")) {
            meetingTimes.add(new MeetingTime(Day.FRIDAY, startTime, endTime));
        }

        return meetingTimes;
    }

    public StringBuilder getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = new StringBuilder(query);
    }
}
