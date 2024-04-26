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
    private boolean rebuildingQuery;
    private ArrayList<SearchFilter> filters;
    private ArrayList<SearchFilter> keywordFilters;
    private ArrayList<Course> results;
    private final int COURSE_CODE_MAX = 699;

    /**
     * Constructor for DatabaseSearch
     * @param conn from Registrar instance
     */
    public DatabaseSearch(Connection conn) {
        this.conn = conn;
        this.filters = new ArrayList<>();
        this.keywordFilters = new ArrayList<>();
        this.searchTerm = "";
        resetQuery();

        filtered = false;
        rebuildingQuery = false;
    }

    /**
     * Searches for courses based on user input in search bar.
     * Searches name, department, code, and instructor for full search term and each word
     * in search term.
     * @param userQuery
     * @return
     */
    public void keywordSearch(String userQuery) {
        setSearchTerm(userQuery);
        resetQuery();
        rebuildingQuery = true;

        applySearchTermFilter(Filter.NAME, userQuery);
        applySearchTermFilter(Filter.DEPARTMENT, userQuery);
        String[] words = userQuery.strip().split(" ");
        if(words.length > 1) {
            searchForDeptAndCode(words);

            //Advanced search
//            for (String word : words) {
//                applySearchTermFilter(Filter.NAME, word);
//                applySearchTermFilter(Filter.DEPARTMENT, word);
//            }
        }
        query.append(")");
        rebuildQueryWithFilters();
        rebuildingQuery = false;

        executeQuery();
    }

    private void searchForDeptAndCode(String[] words) {
        String dept;
        int code;
        String deptCode;
        boolean end = false;
        if(words.length < 2) {return;}

        int i = 1;
        while (!end && i < words.length) {
            code = scanKeyInt(words[i]);
            if((code > 100) && (code < COURSE_CODE_MAX)) {
                dept = words[i - 1].toUpperCase();
                deptCode = dept + " " + code;
                applySearchTermFilter(Filter.DEPARTMENT_CODE, deptCode);
                end = true;
            }
            i += 1;
        }
    }

    /**
     * Applies filter clause to query for search terms without executing the query.
     * @param filter
     * @param userQuery
     */
    public void applySearchTermFilter(Filter filter, String userQuery) {
        addLeadingSearchTermKeyword();
        SearchFilter searchFilter = new SearchFilter(filter, userQuery);
        keywordFilters.add(searchFilter);
        query.append(searchFilter.getClause());
    }

    /**
     * Applies a search filter to the query and executes the search.
     * @param filter
     * @param userQuery
     */
    public void applyFilter(Filter filter, String userQuery) {
        addLeadingFilterKeyword();
        SearchFilter searchFilter = new SearchFilter(filter, userQuery);
        filters.add(searchFilter);
        query.append(searchFilter.getClause());

        executeQuery();
    }

    /**
     * Removes a search filter, and rebuilds and executes the search query.
     * @param type
     * @param key
     */
    public void removeFilter(Filter type, String key) {
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
        if(removedFilter) {
            resetQuery();
            rebuildingQuery = true;
            rebuildQueryWithKeywordFilters();
            rebuildQueryWithFilters();
        }

        executeQuery();
    }

    /**
     * Converts the query to a prepared statement and inserts filter and search term filter values.
     */
    private void prepareQuery() {
        try {
            ps = conn.prepareStatement(String.valueOf(query + ";"));
            int i = 1;

            for(SearchFilter filter : keywordFilters) {
                i = insertFilterValues(filter, i);
            }
            for(SearchFilter filter : filters) {
                i = insertFilterValues(filter, i);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes the database search.
     * @return ArrayList of results as course objects
     */
    private void executeQuery() {
        try {
            prepareQuery();
            ResultSet rs = ps.executeQuery();
            results = readCourseResults(rs);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Parse ResultSet from query execution into Course objects.
     * @param rs
     * @return ArrayList of results as Course objects
     */
    public static ArrayList<Course> readCourseResults(ResultSet rs) {
        ArrayList<Course> results = new ArrayList<>();

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
     * Resets the query to select all courses.
     */
    private void resetQuery() {
        query = new StringBuilder("SELECT * FROM course");
        filtered = false;
    }

    /**
     * Appends search term filter clauses to query.
     */
    private void rebuildQueryWithKeywordFilters() {
        if(!keywordFilters.isEmpty()) {
            for (SearchFilter filter : keywordFilters) {
                addLeadingSearchTermKeyword();
                query.append(filter.getClause());
            }
            query.append(")");
        }
    }

    /**
     * Appends filter (not search term filter) clauses to query.
     */
    private void rebuildQueryWithFilters() {
        for (SearchFilter filter : filters) {
            addLeadingFilterKeyword();
            query.append(filter.getClause());
        }
    }

    /**
     * Adds "WHERE" or "AND" to query.
     */
    private void addLeadingFilterKeyword() {
        if((filters.isEmpty() && keywordFilters.isEmpty()) ||
                (rebuildingQuery && !filtered) ){
            query.append(" WHERE ");
            filtered = true;
        } else {
            query.append(" AND ");
        }
    }
    /**
     * Adds "WHERE" or "OR" to query.
     */
    private void addLeadingSearchTermKeyword() {
        if((filters.isEmpty() && keywordFilters.isEmpty()) ||
                (rebuildingQuery && !filtered) ){
            query.append(" WHERE (");
            filtered = true;
        } else {
            query.append(" OR ");
        }
    }

    /**
     * Inserts filter values in prepared statement.
     * @param filter
     * @param i as a counter for setting filter and search term filter values in prepared statement
     * @return incremented i
     * @throws SQLException
     */
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
            case Filter.DEPARTMENT_CODE:
                setDeptCodeValues(filter.getKey(), i);
                i += 1;
                break;
        }
        return i + 1;
    }

    /**
     * @param key in the form "[DEPT] [CODE]" (e.g., "COMP 350")
     * @param i
     */
    private void setDeptCodeValues(String key, int i) throws SQLException {
        String dept;
        int code;

        String[] deptCode = key.toUpperCase().strip().split(" ");
        dept = deptCode[0];
        code = scanKeyInt(deptCode[1]);
        ps.setString(i, dept);
        ps.setInt(i + 1, code);
    }

    /**
     * @param key
     * @return key as an integer, or -999 if not possible
     */
    private int scanKeyInt(String key) {
        Scanner scanner = new Scanner(key);
        while(scanner.hasNextInt()) {
            return scanner.nextInt();
        }

        return -999;
    }

    /**
     * @param key
     * @return key as a Time (java.sql.Time) object
     */
    private Time scanKeyTime(String key) {
        return Time.valueOf(key);
    }

    /**
     * @param key in the form "[SEMESTER] [YEAR]" (e.g., "Spring 2024")
     * @return Term object
     */
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

    /**
     * Specifies values in prepared statement based on day fields in course table in database.
     * @param key in the format "MWF" or "TR"
     * @param i
     * @throws SQLException
     */
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
    private static ArrayList<MeetingTime> setMeetingTimes(String monday, String tuesday, String wednesday, String thursday, String friday, LocalTime startTime, LocalTime endTime) {
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

    /**
     * Sets searchTerm to value and clears search term filters
     * @param searchTerm
     */
    public void setSearchTerm(String searchTerm) {
        keywordFilters = new ArrayList<>();
        this.searchTerm = searchTerm;
    }
    public String getSearchTerm() {
        return searchTerm;
    }
    public StringBuilder getQuery() {
        return query;
    }
    public ArrayList<Course> getResults() {
        return results;
    }
}
