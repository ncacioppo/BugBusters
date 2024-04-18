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
                this.clause = "WHERE Dept = ?";
                break;
            case Filter.ID:
                this.clause = "WHERE CourseID = ?";
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
