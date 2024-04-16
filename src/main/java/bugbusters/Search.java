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

    /**
     *
     * @return A full list of courses of all the data provided to use by Dr. Hutchins
     */
    public List<Course> getAllCoursesFromExcel() {
        int idCount = 1;

        ArrayList<Course> out = new ArrayList<>();

        Queue<Course> courses2019 = new LinkedList<>();
        Queue<ArrayList<String>> data2019 = Excel.csv.read("2018-2019.csv");
        data2019.poll();
        while (data2019.peek() != null) {
            ArrayList<String> line = data2019.poll();
            Course course = new Course(line);
            course.setId(idCount);
            idCount += 1;
            courses2019.add(course);
        }

        Queue<Course> courses2020 = new LinkedList<>();
        Queue<ArrayList<String>> data2020 = Excel.csv.read("2019-2020.csv");
        data2020.poll();
        while (data2020.peek() != null) {
            ArrayList<String> line = data2020.poll();
            Course course = new Course(line);
            course.setId(idCount);
            idCount += 1;
            courses2020.add(course);
        }

        Queue<Course> courses2021 = new LinkedList<>();
        Queue<ArrayList<String>> data2021 = Excel.csv.read("2020-2021.csv");
        data2021.poll();
        while (data2021.peek() != null) {
            ArrayList<String> line = data2021.poll();
            Course course = new Course(line);
            course.setId(idCount);
            idCount += 1;
            courses2021.add(course);
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

    /**
     * Searches the given list of courses by keyword.
     * @param courses the list of courses that will be searched from
     * @param keyword the keyword being looked for in any course
     * @return the list of courses containing an attribute pertaining to the keyword
     */
    public List<Course> byKeyword(List<Course> courses, String keyword) {
        List<Course> results = new ArrayList<Course>();

        // Starts the search by checking the department.
        // Adds all courses at once, as there is no need to check
        // a newly initialized ArrayList for courses it already contains
        results.addAll(byDepartment(courses, keyword));

        // Prioritizes search by professor next
        for (Course course : byProfessor(courses, keyword)){
            if(!results.contains(course)){
                results.add(course);
            }
        }

        // Follows with search by course name
        for (Course course : byName(courses, keyword)) {
            if (!results.contains(course)) {
                results.add(course);
            }
        }

        // Finishes with searching by description, as often keywords
        // are found before having to check descriptions.
        // This search is case-insensitive.
        for (Course course : courses) {
            if(course.getDescription().toLowerCase().contains(keyword.toLowerCase())){
                if(!results.contains(course)){
                    results.add(course);
                }
            }
        }

        return results;
    }

    /**
     * Searches the given list of courses by course name.
     * @param courses the list of courses that will be searched from
     * @param name the name of the course
     * @return the list of courses containing the keyword in their name
     */
    public List<Course> byName(List<Course> courses, String name) {
        List<Course> results = new ArrayList<Course>();

        // Each course name that contains the given string is a valid result.
        // This search is case-insensitive.
        for (Course course : courses) {
            if(course.getName().toLowerCase().contains(name.toLowerCase())){
                results.add(course);
            }
        }

        return results;
    }

    /**
     * Searches the given list of courses by department.
     * @param courses the list of courses that will be searched from
     * @param department the name of the department, as listed in the course code
     *                   (i.e., COMP for Computer Science classes)
     * @return the list of courses ran by the given department
     */
    public List<Course> byDepartment(List<Course> courses, String department) {
        List<Course> results = new ArrayList<Course>();

        // Each course code that contains the given
        // department abbreviation is a valid result.
        // This search is case-insensitive.
        for (Course course : courses) {
            if(course.getDepartment().toLowerCase().contains(department.toLowerCase())){
                results.add(course);
            }
        }

        return results;
    }

    //cod should be in the format "start(inclusive)-end(inclusive)

    /**
     * This method sorts a list of courses by a range of codes
     * @param courses The list of courses to be searched
     * @param code The range of codes in the format "start-end" where start and end are inclusive
     * @return A list of courses that meets the search criteria
     */
    public List<Course> byCode(List<Course> courses, String code) {
        ArrayList<Course> out = new ArrayList<>();
        String[] listInput = code.split("-");
        int start = Integer.parseInt(listInput[0]);
        int end = Integer.parseInt(listInput[1]);

        for (Course course : courses){
            int courseCode = course.getCode();
            if ((courseCode >= start)&&(courseCode <= end)){
                out.add(course);
            }
        }


        return out;
    }

    /**
     * This method searches a list of courses for courses from a specific term
     * @param courses The list of courses to be searched
     * @param term The string format of the term you would like to search the courses by
     * @return A list of courses that are from the specific searched term
     */
    public List<Course> byTerm(List<Course> courses, String term) {
        ArrayList<Course> out = new ArrayList<>();

        System.out.println(term);

        Term query = new Term(term);
        System.out.println(query);


        for (Course course : courses){
            if (course.getTerm().equals(query)){
                out.add(course);
            }
        }
        return out;
    }

    /**
     * Searches the given list of courses by professor.
     * @param courses the list of courses that will be searched from
     * @param professor the name of the professor
     * @return the list of courses taught by the given professor
     */
    public List<Course> byProfessor(List<Course> courses, String professor) {
        List<Course> results = new ArrayList<Course>();

        // Each course instructor's name that contains
        // the given string is a valid result.
        // This search is case-insensitive.
        for (Course course : courses) {
            if(course.getInstructor().toLowerCase().contains(professor.toLowerCase())){
                results.add(course);
            }
        }

        return results;
    }

    /**
     * This method searches for courses that have the exact ID provided
     * @param courses The list of courses to be searched
     * @param id The specific id you would like to search by
     * @return A list of courses that have the id searched for
     */
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

    /**
     * Searches the given list of courses by meeting days.
     * @param courses the list of courses that will be searched from
     * @param day the day for which a class meets
     * @return the list of courses that meets on the given day
     */
    public List<Course> byDay(List<Course> courses, String day) {
        // Immediately returns all courses if a day is not specified
        if (day.equals(null) || day.equals("")) return courses;

        List<Course> results = new ArrayList<>();

        // Checks each course to see if it is a valid result.
        for (Course course : courses) {
            if (course.getMeetingTimes() == null){
//                results.add(course);
            } else {
                // Grabs the course's meeting times for easy access
                ArrayList<MeetingTime> courseMeetingTimes = course.getMeetingTimes();

                // Boolean to track if any meeting time occurs on the desired day
                boolean correctDay = false;

                // Checks each of the course's meeting times to
                // see if any fall on the appropriate day.
                // This search is case-insensitive.
                for (MeetingTime time : courseMeetingTimes) {
                    if (time.getDay().toString().toLowerCase().equals(day.toLowerCase())) {
                        correctDay = true;
                        break; // no need to check remaining meeting times if the course is on the right day
                    }
                }

                // If a meeting time is found to occur on the
                // correct day, it is a valid search result
                if (correctDay) {
                    if (!results.contains(course)) {
                        results.add(course);
                    }
                }
            }
        }

        return results;
    }

    /**
     * Searches the given list of courses by time range.
     * @param courses the list of courses that will be searched from
     * @param query the range of time during which classes should be taught
     *              (ex: 9:30-12:00)
     * @return the list of courses that meets on the given day
     */
    public List<Course> withinTime(List<Course> courses, String query) {
        List<Course> results = new ArrayList<>(courses);

        String[] input = query.split("-");
        String startTime = input[0];
        String endTime = input[1];

        LocalTime startTimeRange = LocalTime.of(Integer.parseInt(startTime.split(":")[0]), Integer.parseInt(startTime.split(":")[1]), 0);

        LocalTime endTimeRange = LocalTime.of(Integer.parseInt(endTime.split(":")[0]), Integer.parseInt(endTime.split(":")[1]), 0);

        for (Course course : courses) {
            ArrayList<MeetingTime> courseMeetingTimes = course.getMeetingTimes();

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
