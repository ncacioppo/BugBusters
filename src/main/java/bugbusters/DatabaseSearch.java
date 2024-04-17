package bugbusters;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseSearch {
    private Connection conn;
    private StringBuilder query;   //prepared statement for querying courses

    public DatabaseSearch(Connection conn) {
        this.conn = conn;
        this.query = new StringBuilder("SELECT * FROM courses");
    }

    public ArrayList<Course> executeQuery() {
        query.append(";");
        try {
            PreparedStatement ps = conn.prepareStatement(String.valueOf(query));
            //execute & create course objects
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void byKeyword(String keyword) {

    }

}
