package bugbusters;

import com.mysql.cj.SimpleQuery;
import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import javafx.util.Pair;

public class Schedule {
    String name;
    Term term;
    ArrayList<Course> courses;
    ArrayList<Course> events;
    int scheduleID;
    int userID;
    Stack<Pair<String, Course>> undoStack;
    Stack<Pair<String, Course>> redoStack;
    Stack<Pair<Course, Course>> undoResolvedConflicts;
    Stack<Pair<Course, Course>> redoResolvedConflicts;
    public Pair<Course, Course> currentConflict;


    // used for testing
    public Schedule(int userID, String name, Term term, List<Course> courses, List<Course> events){
        setName(name);
        setTerm(term);
        setCourses(courses);
        setEvents(events);
        setUserID(userID);
        // scheduleID defaults to 0 because the database starts generating IDs at 1,
        // so an ID of 0 signifies a schedule that has not been saved yet
        setScheduleID(0);

        undoStack = new Stack<>();
        redoStack = new Stack<>();
        undoResolvedConflicts = new Stack<>();
        redoResolvedConflicts = new Stack<>();
    }

    // actual constructor
    public Schedule(User user, String name, Term term, List<Course> courses, List<Course> events){
        setName(name);
        setTerm(term);
        setCourses(courses);
        setEvents(events);
        setUserID(user.getUserID());
        // scheduleID defaults to 0 because the database starts generating IDs at 1,
        // so an ID of 0 signifies a schedule that has not been saved yet
        setScheduleID(0);
        user.addSchedule(this);

        undoStack = new Stack<>();
        redoStack = new Stack<>();
        undoResolvedConflicts = new Stack<>();
        redoResolvedConflicts = new Stack<>();
    }

    public Schedule(Schedule schedule) {
        setName(schedule.getName());
        setTerm(schedule.getTerm());
        setCourses(schedule.getCourses());
        setEvents(schedule.getEvents());
        setUserID(schedule.getUserID());
        setScheduleID(schedule.getScheduleID());

        undoStack = new Stack<>();
        redoStack = new Stack<>();
        undoResolvedConflicts = new Stack<>();
        redoResolvedConflicts = new Stack<>();
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

    private void setEvents(List<Course> events){
        this.events = new ArrayList<>(events);
    }

    public List<Course> getEvents(){
        return events;
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
//    private boolean isValid(){ // for without events
//        // loop through our courses
//        for (Course course1 : courses) {
//            ArrayList<MeetingTime> course1Times = course1.getMeetingTimes();
//            // loop through every course for every course
//            for (Course course2 : courses) {
//                // if the courses are different
//                if (!course1.equals(course2)) {
//                    ArrayList<MeetingTime> course2Times = course2.getMeetingTimes();
//                    // for the meeting times of course 1
//                    for (MeetingTime course1MT : course1Times) {
//                        // compare to the meeting times of course 2
//                        for (MeetingTime course2MT : course2Times) {
//                            // if the course times overlap return false
//                            if (course1MT.getDay().equals(course2MT.getDay())) {
//                                if (course1MT.getStartTime().isBefore(course2MT.getEndTime())
//                                        && course1MT.getEndTime().isAfter(course2MT.getStartTime())) {
//                                    return false;
//                                }
//                            }
//                        }
//                    }
//                }
//                // if course1 and course2 are two sections of the same class
//                if (course1.getName().equals(course2.getName())
//                && course1.getSection() != course2.getSection()) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

    private boolean eventsAreValid(){
        // loop through our courses
        for (Course course1 : courses) {
            if (checkCourseConflict(course1)) {
                return false;
            } else if (checkEventConflict(course1)) {
                return false;
            }
        }
        // loop through our events
        for (Course event1 : events) {
            if (checkCourseConflict(event1)) {
                return false;
            } else if (checkEventConflict(event1)) {
                return false;
            }
        }

        return true;
    }

    public boolean checkCourseConflict(Course course1) {
        ArrayList<MeetingTime> course1Times = course1.getMeetingTimes();
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
                                currentConflict = new Pair<>(course2, course1);
                                return true;
                            }
                        }
                    }
                }
            }
            if (course1.getName().equals(course2.getName())
                    && course1.getSection() != course2.getSection()) {
                currentConflict = new Pair<>(course2, course1);
                return true;
            }
        }
        return false;
    }

    public boolean checkEventConflict(Course course1) {
        ArrayList<MeetingTime> course1Times = course1.getMeetingTimes();
        for (Course event : events) {
            // if the courses are different
            if (!course1.equals(event)) {
                ArrayList<MeetingTime> eventTimes = event.getMeetingTimes();
                // for the meeting times of course 1
                for (MeetingTime course1MT : course1Times) {
                    // compare to the meeting times of course 2
                    for (MeetingTime eventMT : eventTimes) {
                        // if the course times overlap return false
                        if (course1MT.getDay().equals(eventMT.getDay())) {
                            if (course1MT.getStartTime().isBefore(eventMT.getEndTime())
                                    && course1MT.getEndTime().isAfter(eventMT.getStartTime())) {
                                currentConflict = new Pair<>(event, course1);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

//    public void findConflict(Course course1){
//        ArrayList<MeetingTime> course1Times = course1.getMeetingTimes();
//
//        // loop through every course for every course
//        for (Course course2 : courses) {
//            // if the courses are different
//            if (!course1.equals(course2)) {
//                ArrayList<MeetingTime> course2Times = course2.getMeetingTimes();
//                // for the meeting times of course 1
//                for (MeetingTime course1MT : course1Times) {
//                    // compare to the meeting times of course 2
//                    for (MeetingTime course2MT : course2Times) {
//                        // if the course times overlap return false
//                        if (course1MT.getDay().equals(course2MT.getDay())) {
//                            if (course1MT.getStartTime().isBefore(course2MT.getEndTime())
//                                    && course1MT.getEndTime().isAfter(course2MT.getStartTime())) {
//                                currentConflict = new Pair<>(course2, course1);
//                                return;
//                            }
//                        }
//                    }
//                }
//            }
//            // if course1 and course2 are two sections of the same class
//            if (course1.getName().equals(course2.getName())
//                    && course1.getSection() != course2.getSection()) {
//                currentConflict = new Pair<>(course2, course1);
//                return;
//            }
//        }
//    }

    public void resolveConflict(Course kept, Course removed){
        conflictResolveRemoveCourse(removed);
        conflictResolveAddCourse(kept);

        undoStack.push(new Pair<>("C", kept));
        undoResolvedConflicts.push(new Pair<>(kept, removed));
        redoResolvedConflicts.clear();

        Changelog.logChange("Schedule conflict resolved in " + name +
                ", keeping course " + kept.getName() + ", discarding " + removed.getName());
        // todo: might need to check for more issues caused with undo/redo
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
                if (scheduleCopy.eventsAreValid()) {
                    this.courses.add(course);

                    undoStack.push(new Pair<>("A", course));
                    redoStack.clear();

                    sort();
                    //quickSort(0, this.courses.size()-1);

                    Changelog.logChange("Added course" + course.getName() + " to schedule " + name);
                    return true;
                }
            }
        }
        return false;
    }

    // called by UI interaction
    public boolean addEvent(String name, ArrayList<MeetingTime> meetingTimes){
        Course event = new Course(0, name, "Extracurricular", "N/A",
                0, this.term, ' ', "N/A", meetingTimes, 0);

        Schedule scheduleCopy = new Schedule(this);
        // if our schedule doesn't already contain this course
        if (!scheduleCopy.getEvents().contains(event))  {
            scheduleCopy.getEvents().add(event);
            // if our resulting schedule is valid, add it to the real schedule
            if (scheduleCopy.eventsAreValid()) {
                this.courses.add(event);

                undoStack.push(new Pair<>("AE", event));
                redoStack.clear();

                Changelog.logChange("Added event" + event.getName() + " to schedule " + name);

                return true;
            }
        }
        return false;
    }

    public boolean addEvent(Course event){
        Schedule scheduleCopy = new Schedule(this);
        // if our schedule doesn't already contain this course
        if (!scheduleCopy.getEvents().contains(event))  {
            scheduleCopy.getEvents().add(event);
            // if our resulting schedule is valid, add it to the real schedule
            if (scheduleCopy.eventsAreValid()) {
                this.courses.add(event);

                undoStack.push(new Pair<>("AE", event));
                redoStack.clear();

                Changelog.logChange("Added event" + event.getName() + " to schedule " + name);

                return true;
            }
        }
        return false;
    }

    public boolean conflictResolveAddCourse(Course course){
        if (course != null) {
            Schedule scheduleCopy = new Schedule(this);
            // if our schedule doesn't already contain this course
            if (!scheduleCopy.getCourses().contains(course))  {
                scheduleCopy.getCourses().add(course);
                // if our resulting schedule is valid, add it to the real schedule
                if (scheduleCopy.eventsAreValid()) {
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

                undoStack.push(new Pair<>("R", removed));
                redoStack.clear();

                sort();
                //quickSort(0, this.courses.size()-1);

                Changelog.logChange("Removed course" + course.getName() + " from schedule " + name);

                return removed;
            }
        }
        return null;
    }

    public Course removeEvent(Course event){
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).equals(event)) {
                Course removed = courses.get(i);
                courses.remove(i);

                undoStack.push(new Pair<>("RE", removed));
                redoStack.clear();

                Changelog.logChange("Removed event" + event.getName() + " from schedule " + name);

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

                undoStack.push(new Pair<>("R", removed));
                redoStack.clear();

                sort();
                //quickSort(0, this.courses.size()-1);

                Changelog.logChange("Removed course" + removed.getName() + " from schedule " + name);

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

                undoStack.push(new Pair<>("R", removed));
                redoStack.clear();

                sort();
                //quickSort(0, this.courses.size()-1);

                Changelog.logChange("Removed course" + removed.getName() + " from schedule " + name);

                return removed;
            }
        }
        return null;
    }

    public Course conflictResolveRemoveCourse(Course course){
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

    public Course undoChange(){
        if(undoStack.isEmpty()) return null;
        Pair<String, Course> undo = undoStack.pop();
        Course changed = undo.getValue();
        if (undo.getKey().equals("A")) {
            removeCourse(changed);
            redoStack.push(undo);
        } else if (undo.getKey().equals("R")) {
            addCourse(changed);
            redoStack.push(undo);
        } else if (undo.getKey().equals("AE")) {
            removeEvent(changed);
            redoStack.push(undo);
        } else if (undo.getKey().equals("RE")) {
            addEvent(changed);
            redoStack.push(undo);
        } else if (undo.getKey().equals("C")) {
            undoResolveConflict();
        }
        return changed;
    }

    public Course redoChange(){
        if(redoStack.isEmpty()) return null;
        Pair<String, Course> redo = redoStack.pop();
        Course changed = redo.getValue();
        if (redo.getKey().equals("A")) {
            addCourse(changed);
            undoStack.push(redo);
        } else if (redo.getKey().equals("R")) {
            removeCourse(changed);
            undoStack.push(redo);
        } else if (redo.getKey().equals("AE")) {
            addEvent(changed);
            undoStack.push(redo);
        } else if(redo.getKey().equals("RE")) {
            removeEvent(changed);
            undoStack.push(redo);
        } else if (redo.getKey().equals("C")) {
            redoResolveConflict();
        }
        return changed;
    }

    public void undoResolveConflict(){
        Pair<Course, Course> undo = undoResolvedConflicts.pop();

        Course removed = undo.getKey();

        conflictResolveRemoveCourse(removed);
        conflictResolveAddCourse(undo.getValue());

        redoResolvedConflicts.push(undo);
        redoStack.push(new Pair<>("C", removed));
    }

    public void redoResolveConflict(){
        Pair<Course, Course> redo = redoResolvedConflicts.pop();

        Course added = redo.getKey();

        conflictResolveAddCourse(added);
        conflictResolveRemoveCourse(redo.getValue());

        redoResolvedConflicts.push(redo);
        redoStack.push(new Pair<>("C", added));
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

    public void setCoursesFromDB(Connection conn) {
        try {
            PreparedStatement ps = conn.prepareStatement("" +
                    "SELECT c.* " +
                    "FROM " +
                        "(SELECT * " +
                        "FROM schedule_course " +
                        "WHERE UserID = ?) AS s " +
                    "LEFT JOIN course AS c " +
                    "USING(CourseID);");
            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();
            setCourses(DatabaseSearch.readCourseResults(rs));
        } catch(SQLException e) {
            System.out.println(e.getMessage());
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
