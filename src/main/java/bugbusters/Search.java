package bugbusters;

import java.util.List;
import java.util.Map;

public class Search {
    private Map<Filter, String> filters;

    public Search(Map<Filter, String> filters) {}

    public Search(Search search) {}

    private boolean addFilter(Filter filter, String query) {
        return false;
    }

    public List<Course> getAllCourses() {return null;}
    
    public Filter removeFilter(Filter filter) {return null;}

    public List<Course> byKeyword(List<Course> courses, String keyword) {return null;}

    public List<Course> byName(List<Course> courses, String name) {return null;}

    public List<Course> byDepartment(List<Course> courses, String department) {return null;}

    public List<Course> byCode(List<Course> courses, String code) {return null;}

    public List<Course> byTerm(List<Course> courses, String term) {return null;}

    public List<Course> byProfessor(List<Course> courses, String professor) {return null;}

    public List<Course> byID(List<Course> courses, String id) {return null;}

    public List<Course> byDay(List<Course> courses, String day) {return null;}

    public List<Course> withinTime(List<Course> courses, String startTime, String endTime) {return null;}

    public boolean equals(Object other) {return false;}

    public String toString() {return null;}


    public List<Filter> getFilters() {
        return null;
    }

    public void setFilters(List<Filter> filters) {}
}
