package bugbusters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Schedule {
    String name;
    Term term;
    ArrayList<Course> courses;
    int scheduleID;
    int userID;

    // used for testing
    public Schedule(int userID, String name, Term term, List<Course> courses){
        setName(name);
        setTerm(term);
        setCourses(courses);
        setUserID(userID);
        // scheduleID defaults to 0 because the database starts generating IDs at 1,
        // so an ID of 0 signifies a schedule that has not been saved yet
        setScheduleID(0);
    }

    // actual constructor
    public Schedule(User user, String name, Term term, List<Course> courses){
        setName(name);
        setTerm(term);
        setCourses(courses);
        setUserID(user.getUserID());
        // scheduleID defaults to 0 because the database starts generating IDs at 1,
        // so an ID of 0 signifies a schedule that has not been saved yet
        setScheduleID(0);
        user.addSchedule(this);
    }

    public Schedule(Schedule schedule) {
        setName(schedule.getName());
        setTerm(schedule.getTerm());
        setCourses(schedule.getCourses());
        setUserID(schedule.getUserID());
        setScheduleID(schedule.getScheduleID());
    }

    private void setName(String name) {this.name = name; }

    public String getName(){
        return name;
    }

    private void setTerm(Term term){
        this.term = term;
    }

    public Term getTerm(){
        return term;
    }

    private void setCourses(List<Course> courses){
        this.courses = new ArrayList<>(courses);
    }

    public List<Course> getCourses(){
        return courses;
    }

    private void setUserID(int userID) {
        this.userID = userID;
   }

    public int getUserID() {
        return userID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    /**
     * This method is called whenever we add a new course to the schedule, and checks the following criteria:
     *      - does a section of the course already exist in the schedule?
     *      - does this course overlap with a course already in the schedule?
     * @return true if the course is valid, and false otherwise
     */
    private boolean isValid(){
        // loop through our courses
        for (Course course1 : courses) {
            ArrayList<MeetingTime> course1Times = course1.getMeetingTimes();
            // loop through every course for every course
            for (Course course2 : courses) {
                // if the courses are different
                if (!course1.equals(course2)) {
                    ArrayList<MeetingTime> course2Times = course2.getMeetingTimes();
                    // for the meeting times of course 1
                    for (MeetingTime course1MT : course1Times) {
                        // compare to the meeting times of course 2
                        for (MeetingTime course2MT : course2Times) {
                            // if the course times overlap return false
                            if (course1MT.getDay().equals(course2MT.getDay())) {
                                if (course1MT.getStartTime().isBefore(course2MT.getEndTime())
                                        && course1MT.getEndTime().isAfter(course2MT.getStartTime())) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                // if course1 and course2 are two sections of the same class
                if (course1.getName().equals(course2.getName())
                && course1.getSection() != course2.getSection()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Adds a course to our list of courses. Calls IsValid and includes an additional check to ensure the course is
     * allowed to be added.
     * @param course the course to be saved
     * @return true if the course was saved, and false otherwise
     */
    public boolean addCourse(Course course){
        if (course != null) {
            Schedule scheduleCopy = new Schedule(this);
            // if our schedule doesn't already contain this course
            if (!scheduleCopy.getCourses().contains(course))  {
                scheduleCopy.getCourses().add(course);
                // if our resulting schedule is valid, add it to the real schedule
                if (scheduleCopy.isValid()) {
                    this.courses.add(course);
                    sort();
                    //quickSort(0, this.courses.size()-1);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @return the course that was removed; null if that course did not exist
     */
    public Course removeCourse(Course course){
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).equals(course)) {
                Course removed = courses.get(i);
                courses.remove(i);
                sort();
                //quickSort(0, this.courses.size()-1);
                return removed;
            }
        }
        return null;
    }

    // Remove a course by course code
    public Course removeCourse(int code){
        Course removed;
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCode() == code) {
                removed = courses.get(i);
                courses.remove(i);
                sort();
                //quickSort(0, this.courses.size()-1);
                return removed;
            }
        }
        return null;
    }

    // Remove a course by course name
    public Course removeCourse(String courseName){
        Course removed;
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getName().equals(courseName)) {
                removed = courses.get(i);
                courses.remove(i);
                sort();
                //quickSort(0, this.courses.size()-1);
                return removed;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object other){
        if (!(other instanceof Schedule schedule)) {
            return false;
        }
        if (!name.equals((schedule.name))) {
            return false;
        } else if (!(term.equals(schedule.term))) {
            return  false;
        } else return courses.equals(schedule.courses);
    }

    // Prints out all courses in a schedule, along with the name and term of the schedule
    // Uses the shortform toString for Course to save space and improve readability
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n").append(term).append("\n").append("\n");
        for (Course course : courses) {
            sb.append(course.shortToString()).append("\n").append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    // Displays two schedules side-by-side
    public static String compareView(Schedule scheduleA, Schedule scheduleB) {
        StringBuilder sb = new StringBuilder();
        String formatString = "%-40s %-40s\n";
        sb.append(String.format(formatString, scheduleA.getName(), scheduleB.getName()));
        sb.append(String.format(formatString, scheduleA.getTerm(), scheduleB.getTerm()));
        int loopLength = scheduleA.getCourses().size();
        if (scheduleB.getCourses().size() > loopLength) {
            loopLength = scheduleB.getCourses().size();
        }
        sb.append("\n");
        for (int i = 0; i < loopLength; i++) {
            sb.append(String.format(formatString,
                    scheduleA.getCourses().get(i).getDepartment() + " " + scheduleA.getCourses().get(i).getCode() + " " + scheduleA.getCourses().get(i).getSection(),
                    scheduleB.getCourses().get(i).getDepartment() + " " + scheduleB.getCourses().get(i).getCode() + " " + scheduleB.getCourses().get(i).getSection()));
            sb.append(String.format(formatString,
                    scheduleA.getCourses().get(i).getName(),
                    scheduleB.getCourses().get(i).getName()
                    ));
            sb.append(String.format(formatString,
                    scheduleA.getCourses().get(i).meetingTimesToString(),
                    scheduleB.getCourses().get(i).meetingTimesToString()
                    ));
            sb.append("\n");
        }
        return sb.toString();
    }

    private void sort() {
        boolean swapped;
        for (int i = 0; i < courses.size()-1; i++) {
            swapped = false;
            for (int j = 0; j < courses.size()-i-1; j++) {
                if (courses.get(j+1).courseBefore(courses.get(j))) {
                    System.out.println(courses.get(j+1).getName() + " is before " + courses.get(j).getName());
                    Course temp = courses.get(j);
                    courses.set(j, courses.get(j+1));
                    courses.set(j+1, temp);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        System.out.println("New sort order: ");
        for (Course course : courses) {
            System.out.println(course.getName());
        }
    }
    // the quicksort sorting algorithm, used to sort the list of courses after a course is added or removed
    // TODO: Fix quicksort
//    private void quickSort(int start, int stop) {
//        if (start < stop) {
//            int partitionIndex = partition(start, stop);
//            quickSort(start, partitionIndex-1);
//            quickSort(partitionIndex+1, stop);
//        }
//    }

    // used in quicksort, does not work yet
//    private int partition(int start, int end) {
//        Course x = courses.get(end);
//        int newPartition = start-1;
//        for (int i = start; i < end-1; i++) {
//            if (courses.get(i).courseBefore(x)) {
//                System.out.println(courses.get(i).getName() + " is before " + x.getName());
//                newPartition++;
//                Course temp = courses.get(newPartition);
//                courses.set(newPartition, courses.get(i));
//                courses.set(i, temp);
//            }
//        }
//        System.out.println("Pre final flip: ");
//        for (Course course: courses) {
//            System.out.println(course.getName());
//        }
//        System.out.println();
//        if (courses.get(newPartition+1).courseBefore(x)) {
//            Course temp = courses.get(newPartition + 1);
//            courses.set(newPartition + 1, x);
//            courses.set(end, temp);
//            System.out.println("Post final flip: ");
//            for (Course course : courses) {
//                System.out.println(course.getName());
//            }
//            System.out.println();
//        }
//        System.out.println("New sort order: ");
//        for (Course course : courses) {
//            System.out.println(course.getName());
//        }
//        return newPartition+1;
//    }


}
