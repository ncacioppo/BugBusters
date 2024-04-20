package bugbusters;

import java.sql.*;
import java.util.Scanner;

public class SearchFilter {
    public Filter getType() {
        return filter;
    }

//    public enum Searchable {
//        COURSEID, COURSENAME, DEPT, COURSECODE, SEMESTER, YEAR, INSTRUCTOR,
//        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY,
//        EQ_STARTTIME, GE_STARTTIME, LE_STARTTIME,
//        EQ_ENDTIME, GE_ENDTIME, LE_ENDTIME
//    }
    private Filter filter;
    private String clause;
    private String key;

    public SearchFilter(Filter filter, String key) {
        this.filter = filter;
        this.key = key;

        switch(filter) {
            case Filter.DEPARTMENT:
                this.clause = "Dept = ?";
                break;
            case Filter.ID:
                this.clause = "CourseID = ?";
                break;
            case Filter.NAME:
                this.clause = "CourseName LIKE ?";
                break;
            case Filter.CODE:
                this.clause = "CourseCode = ?";
                break;
            case Filter.CODE_MIN:
                this.clause = "CourseCode >= ?";
                break;
            case Filter.CODE_MAX:
                this.clause = "CourseCode <= ?";
                break;
            case Filter.TERM:
                this.clause = "Semester = ? AND Year = ?";
                break;
            case Filter.PROFESSOR:
                this.clause = "(FNameInstructor LIKE ? OR " +
                                "LNameInstructor LIKE ? OR " +
                                "PrefFNameInstructor LIKE ?)";
                break;

        }


    }

    private void setKey(String key) {
        this.key = key;
    }

    public String getClause() {
        return clause;
    }

    public String getKey() {
        return key;
    }

}
