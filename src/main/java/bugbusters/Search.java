package bugbusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Search {
    private Map<Filter, String> filters;

    public Search(Map<Filter, String> filters) {

    }

    public Search() {

    }

    private boolean addFilter(Filter filter, String query) {
        return false;
    }

    public List<Course> getAllCourses() {
        return null;
    }

    public Filter removeFilter(Filter filter) {
        return null;
    }

    public List<Course> byKeyword(List<Course> courses, String keyword) {
        List<Course> results = new ArrayList<Course>();

        results.addAll(byDepartment(courses, keyword));

        for (Course course : byProfessor(courses, keyword)){
            if(!results.contains(course)){
                results.add(course);
            }
        }

        for (Course course : byName(courses, keyword)) {
            if (!results.contains(course)) {
                results.add(course);
            }
        }

        for (Course course : courses) {
            if(course.getDescription().toLowerCase().contains(keyword.toLowerCase())){
                if(!results.contains(course)){
                    results.add(course);
                }
            }
        }

        return results;
    }

    public List<Course> byName(List<Course> courses, String name) {
        List<Course> results = new ArrayList<Course>();

        for (Course course : courses) {
            if(course.getName().toLowerCase().contains(name.toLowerCase())){
                results.add(course);
            }
        }

        return results;
    }

    public List<Course> byDepartment(List<Course> courses, String department) {
        List<Course> results = new ArrayList<Course>();

        for (Course course : courses) {
            if(course.getDepartment().toLowerCase().contains(department.toLowerCase())){
                results.add(course);
            }
        }

        return results;
    }

    public List<Course> byCode(List<Course> courses, String code) {
        return null;
    }

    public List<Course> byTerm(List<Course> courses, String term) {
        ArrayList<Course> out = new ArrayList<>();
        Term query = new Term(term);

        for (Course course : courses){
            if (course.getTerm().equals(query)){
                out.add(course);
            }
        }
        return out;
    }

    public List<Course> byProfessor(List<Course> courses, String professor) {
        List<Course> results = new ArrayList<Course>();

        for (Course course : courses) {
            if(course.getInstructor().toLowerCase().contains(professor.toLowerCase())){
                results.add(course);
            }
        }

        return results;
    }

    public List<Course> byID(List<Course> courses, String id) {
        return null;
    }

    public List<Course> byDay(List<Course> courses, String day) {
        return null;
    }

    public List<Course> withinTime(List<Course> courses, String startTime, String endTime) {
        return null;
    }

    public boolean equals(Object other) {
        return false;
    }

    public String toString() {
        return null;
    }

    public List<Filter> getFilters() {
        return filters.keySet().stream().toList();
    }

    public void setFilters(List<Filter> filters) {

    }
}
