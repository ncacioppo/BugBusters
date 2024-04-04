package bugbusters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Run {

    private static final String terminalString = "Grove City College Schedule Maker -> ";
    private static ArrayList<Course> courses;
    private static ArrayList<Schedule> schedules;
    private static User user;
    private static Search search;
    private static Scanner scanner;

    public static void run(){
        user = new User();
        schedules = new ArrayList<>();
        search = new Search();
        courses = new ArrayList<>(search.getAllCoursesFromExcel());
        scanner = new Scanner(System.in);

        while (true){
            System.out.print(terminalString);

            String[] input = scanner.nextLine().toUpperCase().split(" ");

            String query = input[0];

            switch(query) {
                case "SEARCH":
                    runSearch();
                    break;
                case "SCHEDULE":
                    runSchedule();
                    break;
                case "USER":
                    runUser(input);
                    break;
                case "EXIT":
                    System.exit(0);
                    break;
                default:
                    System.out.println("'" + query + "' is not a recognized command.");
            }
        }
    }

    private static void runUser(String[] input){

        if (input.length < 2){
            if (user.getFirstName().equalsIgnoreCase("")) {
                boolean resolved = false;
                String firstName = "";
                String lastName = "";

                while (!resolved) {
                    String tempLastName = "";
                    System.out.println("You have not set up user yet, please enter you first and last name as follows ('first last'):");
                    String names[] = scanner.nextLine().toUpperCase().split(" ");
                    System.out.println("First name: " + names[0]);
                    if (names.length >1){
                        String[] temp = Arrays.copyOfRange(names, 1, names.length);
                        for (String string : temp){
                            tempLastName += string + " ";
                        }
                        tempLastName.trim();
                        System.out.println("Last name: " + tempLastName);
                    } else {
                        tempLastName = "";
                    }
                    System.out.println("Is this correct [y/n] enter 'q' to exit user setup");
                    String response = scanner.nextLine().toUpperCase();
                    if (response.equalsIgnoreCase("y")){
                        firstName = names[0];
                        lastName = tempLastName;
                        resolved = true;
                    } else if (response.equalsIgnoreCase("q")){
                        return;
                    }
                }
                if (resolved){
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                }
            }

            if (user.getCollegeYear() == null){
                Boolean resolved = false;
                System.out.println("You have not set up a college year yet please response with the number for your year in college, here are your options:\n" +
                        "1. FRESHMAN\n" +
                        "2. SOPHOMORE\n" +
                        "3. JUNIOR\n" +
                        "4. SENIOR\n" +
                        "5. SUPERSENIOR");
                String num = scanner.nextLine().toUpperCase();

                while (!resolved) {
                    switch (num) {
                        case "1":
                            user.setCollegeYear(CollegeYear.FRESHMAN);
                            resolved = true;
                            break;
                        case "2":
                            user.setCollegeYear(CollegeYear.SOPHOMORE);
                            resolved = true;
                            break;
                        case "3":
                            user.setCollegeYear(CollegeYear.JUNIOR);
                            resolved = true;
                            break;
                        case "4":
                            user.setCollegeYear(CollegeYear.SENIOR);
                            resolved = true;
                            break;
                        case "5":
                            user.setCollegeYear(CollegeYear.SUPERSENIOR);
                            resolved = true;
                            break;
                        case "q":
                            return;
                        default:
                            System.out.println(num + " is not a valid input, note you can enter 'q' to end user setup");
                    }
                }
            }

            System.out.println(user);
        } else {
            String query = input[1];
            switch (query) {
                case "MAJOR":
                    String[] tempMajor = Arrays.copyOfRange(input, 2, input.length-1);
                    String year = input[input.length-1];
                    String actualMajor = "";
                    for (String string : tempMajor){
                        actualMajor += string + " ";
                    }
                    actualMajor = actualMajor.trim();
                    user.addUserMajor(actualMajor, Integer.parseInt(year));
                    break;
                case "MINOR":
                    user.addUserMinor(input[2], Integer.parseInt(input[3]));
                    break;
                default:
                    System.out.println("Default");
            }
        }
    }

    private static void runSchedule(){

        while (true) {
            System.out.print(terminalString + "Schedule -> ");

            String[] input = scanner.nextLine().toUpperCase().split(" ");

            switch (input[0]){
                case "CREATE":
                    schedules.add(new Schedule(input[1], new Term(input[2] + " " + input[3]), new ArrayList<>()));
                    break;
                case "VIEW":
                    runScheduleView(input[1], input[2]);
                    break;
                case "ADD":
                    if (runAddCourse(input[1], input[2])){
                        System.out.println("Course successfully added to schedule '" + input[1] + "'");
                    } else {
                        System.out.println("An error occurred and the course could not be added to schedule '" + input[1] + "'");
                    }
                    break;
                case "REMOVE":
                    if (runRemoveCourse(input[1], input[2])){
                        System.out.println("Course successfully removed from schedule '" + input[1] + "'");
                    } else {
                        System.out.println("An error occurred and the course could not be removed from schedule '" + input[1] + "'");
                    }
                    break;
                case "ALL":
                    if (schedules.size() > 0) {
                        for (Schedule schedule : schedules) {
                            System.out.println(schedule.name + " - " + schedule.term);
                        }
                    } else {
                        System.out.println("-----There are currently no schedules saved-----");
                    }
                    break;
                case "BACK":
                    return;
                default:
                    System.out.println("Default");
            }
        }

    }

    private static void runScheduleView(String scheduleName, String viewType){

        Schedule currentSchedule = null;

        for (Schedule schedule : schedules){
            if (schedule.name.equalsIgnoreCase(scheduleName)){
                currentSchedule = schedule;
                break;
            }
        }

        if (currentSchedule == null){
            System.out.println("Error finding course");
            return;
        }

        if (viewType.equalsIgnoreCase("CALENDAR")){
            /**
             * TODO
             * use view in calendar format when it is added to master
             */
            System.out.println("Calendar view of " + currentSchedule.name + " - " + currentSchedule.term);
        } else if (viewType.equalsIgnoreCase("LIST")){
            System.out.println(currentSchedule.toString());
        } else {
            System.out.println("There was an error");
        }
    }

    private static Boolean runRemoveCourse(String scheduleName, String courseID){
        Schedule currentSchedule = null;

        for (Schedule schedule : schedules){
            if (schedule.name.equalsIgnoreCase(scheduleName)){
                currentSchedule = schedule;
                break;
            }
        }

        if (currentSchedule == null){
            System.out.println("Error in finding schedule");
            return false;
        }

        Course currentCourse = null;
        for (Course course : courses){
            if (course.getId() == Integer.parseInt(courseID)){
                currentCourse = course;
                break;
            }
        }

        if (currentCourse == null){
            System.out.println("Error finding course");
            return false;
        }


        Course removedCourse = currentSchedule.removeCourse(currentCourse);

        return false;
    }

    private static Boolean runAddCourse(String scheduleName, String courseID){

        Schedule currentSchedule = null;

        for (Schedule schedule : schedules){
            if (schedule.name.equalsIgnoreCase(scheduleName)){
                currentSchedule = schedule;
                break;
            }
        }

        if (currentSchedule == null){
            System.out.println("Error in finding schedule");
            return false;
        }

        Course currentCourse = null;
        for (Course course : courses){
            if (course.getId() == Integer.parseInt(courseID)){
                currentCourse = course;
                break;
            }
        }

        if (currentCourse == null){
            System.out.println("Error finding course");
            return false;
        }


        return currentSchedule.addCourse(currentCourse);
    }

    private static void runSearch(){

        while (true) {
            System.out.print(terminalString + "Search -> ");

            String[] input = (scanner.nextLine().toUpperCase()).split(" ");

            if ((input.length < 2) && (!input[0].equalsIgnoreCase("BACK")) && (!input[0].equalsIgnoreCase("CLEAR")) && (!input[0].equalsIgnoreCase("COURSES"))){
                System.out.println("A filter and a value has to be entered for Search\n");
                System.out.println("Usage: ");
            } else if (input[0].equalsIgnoreCase("BACK")) {
                return;
            } else if (input[0].equalsIgnoreCase("CLEAR")) {
                courses = new ArrayList<>(search.getAllCoursesFromExcel());
                printCourses();
            } else if (input[0].equalsIgnoreCase("COURSES")){
                printCourses();
            } else {
                confirmSearchCourses();
                String filter = input[0];
                ArrayList<String> query = new ArrayList<>();
                int count = 0;
                for (String str : input){
                    if (count == 0){
                    }else{
                        query.add(str);
                    }
                    count += 1;
                }

                switch(filter){
                    case "KEYWORD":
                        courses = new ArrayList<>(search.byKeyword(courses, query.get(0)));
                        printCourses();
                        break;
                    case "NAME":
                        courses = new ArrayList<>(search.byName(courses, query.get(0)));
                        printCourses();
                        break;
                    case "DEPARTMENT":
                        courses = new ArrayList<>(search.byDepartment(courses, query.get(0)));
                        printCourses();
                        break;
                    case "CODE":
                        courses = new ArrayList<>(search.byCode(courses, query.get(0)));
                        printCourses();
                        break;
                    case "TERM":
                        courses = new ArrayList<>(search.byTerm(courses, query.get(0) + " " + query.get(1)));
                        printCourses();
                        break;
                    case "PROFESSOR":
                        courses = new ArrayList<>(search.byProfessor(courses, query.get(0)));
                        printCourses();
                        break;
                    case "ID":
                        courses = new ArrayList<>(search.byID(courses, query.get(0)));
                        printCourses();
                        break;
                    case "DAY":
                        courses = new ArrayList<>(search.byDay(courses, query.get(0)));
                        printCourses();
                        break;
                    case "TIME":
                        courses = new ArrayList<>(search.withinTime(courses, query.get(0)));
                        printCourses();
                        break;
                    case "ADD":
                        runAddCourse(query.get(0), query.get(1));
                        break;
                    default:
                        System.out.println("Searching by " + filter + " is not currently supported.");
                }
            }


        }
    }

    private static void confirmSearchCourses(){
        System.out.print("Would you like to refine the current course list? [Y/N] -> ");

        while (true) {
            String input = scanner.nextLine().toUpperCase();

            switch (input) {
                case "Y":
                    return;
                case "N":
                    courses = new ArrayList<>(search.getAllCoursesFromExcel());
                    return;
                default:
                    System.out.println("Please only enter Y/N. '" + input + "' is not a valid input");
            }
        }
    }

    private static void printCourses(){
        System.out.println("===================Courses===================");
        for (Course course : courses){
            System.out.println("----------------------------");
            System.out.println(course);
            System.out.println("----------------------------");
        }
        System.out.println("=============================================");
    }
}
