package bugbusters;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class Search {
    private Map<Filter, String> filters;

    public Search(Map<Filter, String> filters) {

    }

    public Search() {

    }

    private boolean addFilter(Filter filter, String query) {
        return false;
    }

    public List<Course> getAllCoursesFromExcel() {
        ArrayList<Course> out = new ArrayList<>();

        Queue<Course> courses2019 = new LinkedList<>();
        Queue<ArrayList<String>> data2019 = Excel.csv.read("2018-2019.csv");
        data2019.poll();
        while (data2019.peek() != null) {
            ArrayList<String> line = data2019.poll();
            courses2019.add(new Course(line));
        }

        Queue<Course> courses2020 = new LinkedList<>();
        Queue<ArrayList<String>> data2020 = Excel.csv.read("2019-2020.csv");
        data2020.poll();
        while (data2020.peek() != null) {
            ArrayList<String> line = data2020.poll();
            courses2020.add(new Course(line));
        }

        Queue<Course> courses2021 = new LinkedList<>();
        Queue<ArrayList<String>> data2021 = Excel.csv.read("2020-2021.csv");
        data2021.poll();
        while (data2021.peek() != null) {
            ArrayList<String> line = data2021.poll();
            courses2021.add(new Course(line));
        }

        out.addAll(courses2019);
        out.addAll(courses2020);
        out.addAll(courses2021);

        return out;
    }

    public List<Course> getAllCoursesFromSQL() {
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
        ArrayList<Course> out = new ArrayList<>();

        int query = Integer.parseInt(id);

        for (Course x : courses){
            if (x.getId() == query){
                out.add(x);
            }
        }

        return out;
    }

    public List<Course> byDay(List<Course> courses, String day) {
        if (day.equals(null) || day.equals("")) return courses;

        List<Course> results = new ArrayList<>();

        for (Course course : courses) {
            Set<MeetingTime> courseMeetingTimes = course.getMeetingTimes();

            boolean correctDay = false;

            for (MeetingTime time : courseMeetingTimes){
                if (time.getDay().toString().toLowerCase().equals(day.toLowerCase())) {
                    correctDay = true;
                    break;
                }
            }

            if (correctDay) {
                if (!results.contains(course)) {
                    results.add(course);
                }
            }
        }

        return results;
    }

    public List<Course> withinTime(List<Course> courses, String startTime, String endTime) {
        List<Course> results = new ArrayList<>(courses);
        LocalTime startTimeRange = LocalTime.parse(startTime);
        LocalTime endTimeRange = LocalTime.parse(endTime);

        for (Course course : courses) {
            Set<MeetingTime> courseMeetingTimes = course.getMeetingTimes();

            for (MeetingTime time : courseMeetingTimes) {
                if (time.getStartTime().compareTo(startTimeRange) < 0) {
                    if (results.contains(course)) {
                        results.remove(course);
                    }
                } else if (time.getStartTime().compareTo(endTimeRange) >= 0) {
                    if (results.contains(course)) {
                        results.remove(course);
                    }
                } else if (Duration.between(startTimeRange, endTimeRange).compareTo(time.getDuration()) < 0) {
                    if (results.contains(course)) {
                        results.remove(course);
                    }
                }
            }
        }

        return results;
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
