package bugbusters;

import java.sql.*;
import java.util.Scanner;

public class SearchFilter {
    public Searchable getField() {
        return field;
    }

    public enum Searchable {
        COURSEID, COURSENAME, DEPT, COURSECODE, SEMESTER, YEAR, INSTRUCTOR,
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY,
        EQ_STARTTIME, GE_STARTTIME, LE_STARTTIME,
        EQ_ENDTIME, GE_ENDTIME, LE_ENDTIME
    }
    private Searchable field;
    private String clause;
    private String key;

    public SearchFilter(Searchable field, String key) {
        this.field = field;
        Scanner scanner = new Scanner(key);

        switch(field) {
            case Searchable.DEPT:
                this.clause = "WHERE Dept = ?";
                setKey(key);
                break;
            case Searchable.COURSEID:
                this.clause = "WHERE CourseID = ?";
                if(scanner.hasNextInt()) {
                    scanner.useDelimiter(" ");
//                    setKey(scanner.nextInt());
                }
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
