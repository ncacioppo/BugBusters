package bugbusters;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class DatabaseSearch {
    private Connection conn;
    private StringBuilder query;   //for querying courses
    private PreparedStatement ps;
    private String searchTerm;     //search query from user input
    private boolean filtered;      //true if query has at least one WHERE clause
    private ArrayList<SearchFilter> filters;
    private ArrayList<SearchFilter> keywordFilters;
    private ArrayList<Course> results;

    /**
     * Constructor for DatabaseSearch
     * @param conn from Registrar instance
     */
    public DatabaseSearch(Connection conn) {
        this.conn = conn;
        this.query = new StringBuilder("SELECT * FROM course");
        this.filters = new ArrayList<>();
        this.searchTerm = "";
        filtered = false;
    }

    /**
     * Searches for courses based on user input in search bar.
     * Searches name, department, code, and instructor for full search term and each word
     * in search term.
     * @param userQuery
     * @return
     */
    public ArrayList<Course> keywordSearch(String userQuery) {
        setSearchTerm(userQuery);

        applySearchTermFilter(Filter.NAME, userQuery);
        applySearchTermFilter(Filter.DEPARTMENT, userQuery);

//        String[] words = userQuery.strip().split(" ");
//        for(String word : words) {
//
//        }

        query.append(")");
        return executeQuery();
    }

    public ArrayList<Course> applyFilter(Filter filter, String userQuery) {
        addLeadingFilterKeywords();
        SearchFilter searchFilter = new SearchFilter(filter, userQuery);
        filters.add(searchFilter);
        query.append(searchFilter.getClause());

        return executeQuery();
    }

    private void addLeadingFilterKeywords() {
        if(filters.isEmpty()) {
            query.append(" WHERE ");
            filtered = true;
        } else {
            query.append(" AND ");
        }
    }

    public void applySearchTermFilter(Filter filter, String userQuery) {
        addLeadingSearchTermKeywords();
        SearchFilter searchFilter = new SearchFilter(filter, userQuery);
        keywordFilters.add(searchFilter);
        query.append(searchFilter.getClause());
    }

    private void addLeadingSearchTermKeywords() {
        if(filters.isEmpty()) {
            query.append(" WHERE (");
            filtered = true;
        } else {
            query.append(" OR ");
        }
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    /**
     * Executes this DatabaseSearch's query
     * @return ArrayList of results as course objects
     */
    public ArrayList<Course> executeQuery() {
        try {
            refineQuery();
            ResultSet rs = ps.executeQuery();
            results = getCourseResults(rs);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return results;
    }

    public PreparedStatement refineQuery() {
        try {
            ps = conn.prepareStatement(String.valueOf(query + ";"));
            int i = 1;

            for(SearchFilter filter : filters) {
                i = insertFilterValues(filter, i);
            }
            for(SearchFilter filter : keywordFilters) {
                i = insertFilterValues(filter, i);
            }

            return ps;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private int insertFilterValues(SearchFilter filter, int i) throws SQLException {
        switch(filter.getType()) {
            case Filter.DEPARTMENT:
                ps.setString(i, filter.getKey());
                break;
            case Filter.ID, Filter.CODE, Filter.CODE_MIN, Filter.CODE_MAX:
                ps.setInt(i, scanKeyInt(filter.getKey()));
                break;
            case Filter.NAME:
                String name = "%" + filter.getKey() + "%";
                ps.setString(i, name);
                break;
            case Filter.TERM:
                Term term = convertToTerm(filter.getKey());
                ps.setString(i, term.getSeason());
                i += 1;
                ps.setInt(i, term.getYear());
                break;
            case Filter.PROFESSOR:  //takes one word
                String professor = "%" + filter.getKey() + "%"; //TODO: split into 3 fields
                for(int j = i; j < i + 3; j++) {
                    ps.setString(j, professor);
                }
                i += 2;
                break;
            case Filter.DAY:    //takes string "MWF" or "TR" for toggle filter
                setDayFilterValues(filter.getKey(), i);
                i += 4;
                break;
            case Filter.TIME_MIN, TIME_MAX:
                ps.setTime(i, scanKeyTime(filter.getKey()));
                break;
        }
        return i + 1;
    }

    private void setDayFilterValues(String key, int i) throws SQLException{
        if(key.equals("MWF")) {
            ps.setString(i, "M");
            ps.setString(i + 1, "");
            ps.setString(i + 2, "W");
            ps.setString(i + 3, "");
            ps.setString(i + 4, "F");
            return;
        }
        if(key.equals("TR")) {
            ps.setString(i, "");
            ps.setString(i + 1, "T");
            ps.setString(i + 2, "");
            ps.setString(i + 3, "R");
            ps.setString(i + 4, "");
        }
    }

    private Term convertToTerm(String key) {
        String semester = "";
        int year = 0;

        Scanner scanner = new Scanner(key);
        scanner.useDelimiter(" ");

        if (scanner.hasNext()) {
            semester = scanner.next().toUpperCase();
        }
        if (scanner.hasNextInt()) {
            year = scanner.nextInt();
        }
        return new Term(semester, year);
    }

    private void appendFilterClauses() {
        for(SearchFilter searchFilter : filters) {
            addLeadingSearchTermKeywords();
            query.append(searchFilter.getClause());
        }
    }

    private void appendSearchTermClauses() {
        for(SearchFilter searchFilter : keywordFilters) {
            addLeadingSearchTermKeywords();
            query.append(searchFilter.getClause());
        }
    }
    private int scanKeyInt(String key) {
        Scanner scanner = new Scanner(key);
        while(scanner.hasNextInt()) {
            return scanner.nextInt();
        }

        return -999;
    }

    private Time scanKeyTime(String key) {
        return Time.valueOf(key);
    }

    public ArrayList<Course> removeFilter(Filter type, String key) {
        SearchFilter filter;
        boolean removedFilter = false;

        key = key.toUpperCase();
        for(int i = 0; i < filters.size(); i++) {
            filter = filters.get(i);
            if(type.equals(filter.getType()) && key.equals(filter.getKey())) {
                filters.remove(i);
                removedFilter = true;
            }
        }
        if(filters.isEmpty()) {
            filtered = false;
        } else if(removedFilter) {
            appendSearchTermClauses();
            appendFilterClauses();
        }
        return executeQuery();
    }

    private void resetQuery() {
        query = new StringBuilder("SELECT * FROM course");
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
                char section = 'A';
                String courseSection = rs.getString(5);
                if(!courseSection.isEmpty()) {
                    section = courseSection.charAt(0);
                }
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

    public String getSearchTerm() {
        return searchTerm;
    }
}
