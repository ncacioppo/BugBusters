package bugbusters;

import java.sql.Connection;
import java.util.List;

public class Major {
    private final String majorName;
    private final int reqYr;
    private final String department;


    /**
     * Constructor for Major
     * @param majorName
     * @param reqYr
     */
    public Major(String majorName, int reqYr){
        this.majorName = majorName;
        this.reqYr = reqYr;
        this.department = setDepartment(majorName);
    }

    /**
     * @return name of major as string
     */
    public String getMajorName() {
        return majorName;
    }

    public int getReqYear() { return reqYr; }

    private String setDepartment(String majorName) {
        majorName = majorName.toLowerCase();
        if (majorName.contains("computer science")) {
            return "COMP";
        } else if (majorName.contains("business statistics")) {
            return "MNGT";
        } else if (majorName.contains("accounting")) {
            return "ACCT";
        } else if (majorName.contains("biblical")) {
            return "RELI";
        } else if (majorName.contains("philosophy")) {
            return "PHIL";
        }
        return "";
    }

    public String getDepartment() { return department; }


}
