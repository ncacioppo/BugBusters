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
                case "HELP":
                    System.out.println("Usage:\n" +
                            "SEARCH                                -> To search for courses\n" +
                            "SCHECULE                              -> To view and edit schedules\n" +
                            "USER                                  -> To view user information\n" +
                            "USER MAJOR majorname requirementsyear -> To add a major to user account 'majorname' can be multiple words\n" +
                            "USER MINOR minorname requirementsyear -> To add a minor to user account 'minorname' can be multiple words\n" +
                            "USER REMOVE MAJOR                     -> To remove a major from user account\n" +
                            "USER REMOVE MINOR                     -> To remove a minor from user account\n" +
                            "EXIT                                  -> To exit the application");
                    break;
                default:
                    System.out.println("'" + query + "' is not a recognized command. Try command HELP");
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
                        System.out.println(user);
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
                            System.out.println(user);
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
                    int majorYear;
                    try {
                        majorYear = Integer.parseInt(input[input.length - 1]);
                    } catch (Exception e){
                        System.out.println("Usage of USER MAJOR:\n" +
                                "\"USER MAJOR majorname requirementsyear -> to add a major to user account 'majorname' can be multiple words");
                        break;
                    }
                    String actualMajor = "";
                    for (String string : tempMajor){
                        actualMajor += string + " ";
                    }
                    actualMajor = actualMajor.trim();
                    user.addUserMajor(actualMajor, majorYear);
                    break;
                case "MINOR":
                    String[] tempMinor = Arrays.copyOfRange(input, 2, input.length-1);
                    int minorYear = 2;
                    try {
                        minorYear = Integer.parseInt(input[input.length - 1]);
                    } catch (Exception e){
                        System.out.println("Usage of USER MINOR:\n" +
                                "USER MINOR minorname requirementsyear -> to add a minor to user account 'minorname' can be multiple words");
                        break;
                    }
                    String actualMinor = "";
                    for (String string : tempMinor){
                        actualMinor += string + " ";
                    }
                    actualMinor = actualMinor.trim();
                    user.addUserMinor(actualMinor, minorYear);
                    break;
                case "REMOVE":
                    if (input[2].equalsIgnoreCase("MAJOR")){
                        runRemoveMajor();
                    } else if (input[2].equalsIgnoreCase("MINOR")){
                        runRemoveMinor();
                    } else {
                        System.out.println("Argument after REMOVE van only be MAJOR or MINOR");
                    }
                    break;
                default:
                    System.out.println("Default");
            }
        }
    }

    private static void runRemoveMajor(){
        if (user.getUserMajors().size() >0){
            System.out.println("Here are your listed majors(s), please respond with the number of the major you would like to delete or enter q to cancel: ");
            int count = 1;
            for (Major major : user.getUserMajors()){
                System.out.println(count + ". " + major.getMajorName());
                count += 1;
            }
            String numString = scanner.nextLine();
            if (numString.equalsIgnoreCase("q")){
                return;
            }
            try{
                int num = Integer.parseInt(numString);
                if ((num < 1)||(num > user.getUserMajors().size())){
                    System.out.println("Invalid number");
                } else {
                    user.removeUserMajor(user.getUserMajors().get(num-1).getMajorName());
                    System.out.println("Successfully deleted major");
                }
            } catch(Exception e){
                System.out.println("Error deleting major");
            }
        } else {
            System.out.println("No majors are listed");
        }
    }

    private static void runRemoveMinor(){
        if (user.getUserMajors().size() >0){
            System.out.println("Here are your listed minor(s), please respond with the number of the minor you would like to delete or enter q to cancel: ");
            int count = 1;
            for (Minor minor : user.getUserMinors()){
                System.out.println(count + ". " + minor.getMinorName());
                count += 1;
            }
            String numString = scanner.nextLine();
            if (numString.equalsIgnoreCase("q")){
                return;
            }
            try{
                int num = Integer.parseInt(numString);
                if ((num < 1)||(num > user.getUserMinors().size())){
                    System.out.println("Invalid number");
                } else {
                    user.removeUserMinor(user.getUserMinors().get(num-1).getMinorName());
                    System.out.println("Successfully deleted minor");
                }
            } catch(Exception e){
                System.out.println("Error deleting minor");
            }
        } else {
            System.out.println("No minors are listed");
        }
    }

    private static void runSchedule(){

        while (true) {
            System.out.print(terminalString + "Schedule -> ");

            String[] input = scanner.nextLine().toUpperCase().split(" ");

            switch (input[0]){
                case "CREATE":
                    try {
                        Term createTerm = new Term(input[2] + " " + input[3]);
                        schedules.add(new Schedule(user, input[1], createTerm, new ArrayList<>()));
                    } catch (Exception e){
                        System.out.println("Incorrect usage of CREATE, usage Example:\n" + terminalString + "Schedule -> CREATE schedulename spring 2021");
                    }
                    break;
                case "DELETE":
                    runDeleteSchedule();
                    break;
                case "VIEW":
                    try {
                        runScheduleView(input[1], input[2]);
                    } catch (Exception e){
                        System.out.println("Incorrect usage of VIEW; usage example:\n" + terminalString + "Schedule -> VIEW schedulename LIST\nNOTE** the last argument should only be LIST or CALENDAR");
                    }
                    break;
                case "ADD":
                    try {
                        if (runAddCourse(input[1], input[2])) {
                            System.out.println("Course successfully added to schedule '" + input[1] + "'");
                        } else {
                            System.out.println("An error occurred and the course could not be added to schedule '" + input[1] + "'");
                        }
                    } catch (Exception e){
                        System.out.println("Incorrect usage of ADD; usage example:\n" + terminalString + "Schedule -> ADD schedulename courseID");
                    }
                    break;
                case "REMOVE":
                    try {
                        if (runRemoveCourse(input[1], input[2])) {
                            System.out.println("Course successfully removed from schedule '" + input[1] + "'");
                        } else {
                            System.out.println("An error occurred and the course could not be removed from schedule '" + input[1] + "'");
                        }
                    } catch (Exception e){
                        System.out.println("Incorrect usage of REMOVE; usage example:\n" + terminalString + "Schedule -> REMOVE schedulename courseID");
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
                case "HELP":
                    System.out.println("Usage:\n" +
                            "CREATE scheduleName season year -> To create a new schedule\n" +
                            "DELETE                          -> To delete an existing schedule\n" +
                            "VIEW scheduleName viewType      -> To view an existing schedule in a specific view type. NOTE view type is either LIST or CALENDAR\n" +
                            "ADD scheduleName courseID       -> To add a specific course to an existing schedule\n" +
                            "REMOVE scheduleName courseID    -> To remove a course from an existing course\n" +
                            "ALL                             -> To see a list of all schedules\n" +
                            "BACK                            -> To go back to opening screen");
                    break;
                case "BACK":
                    return;
                default:
                    System.out.println("'" + input[0] + "' is not a recognized command. Try command HELP");
            }
        }

    }

    private static void runDeleteSchedule(){
        System.out.println("Here are all your schedules, please respond with the number of the schedule you would like to delete: ");
        int count = 1;
        for (Schedule schedule : schedules){
            System.out.println(count + ". " + schedule.name + " - " + schedule.term);
            count += 1;
        }
        String numString = scanner.nextLine();
        try{
            int num = Integer.parseInt(numString);
            if ((num < 1)||(num > schedules.size())){
                System.out.println("Error");
            } else {
                Schedule removed = schedules.remove(num-1);
                System.out.println("Successfully deleted " + removed.name + " - " + removed.term);
            }
        } catch(Exception e){
            System.out.println("Error");
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
             * add calendar view once it works and is in master
             */
//            CalendarView view = new CalendarView(currentSchedule);
//            view.printScheduleAsCalendar();
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

        if (removedCourse == null){
            return false;
        } else {
            return true;
        }
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

            if ((input.length < 2) && (!input[0].equalsIgnoreCase("BACK")) && (!input[0].equalsIgnoreCase("CLEAR")) && (!input[0].equalsIgnoreCase("COURSES")) && (!input[0].equalsIgnoreCase("HELP"))) {
                System.out.println("A filter and a value has to be entered for Search\n");
                System.out.println("Usage: ");
            } else if (input[0].equalsIgnoreCase("HELP")){
                System.out.println("Usage:\n" +
                        "BACK                      -> To return to the opening screen\n" +
                        "CLEAR                     -> To clear all previous searches and change current course list to all courses\n" +
                        "COURSES                   -> To view current course list\n" +
                        "KEYWORD query             -> To search for courses in current list that contain the keyword 'query'\n" +
                        "NAME query                -> To search for courses in current list that have the name 'query'\n" +
                        "DEPARTMENT query          -> To search for courses in current list that are for the department 'query'\n" +
                        "CODE query                -> To search for courses in current list with a course code in a certain range. Format of query is 'min-max' where min and max are integers\n" +
                        "TERM query                -> To search for courses in current list for a specific term. The format for query is: season-year ex. spring-2020\n" +
                        "PROFESSOR query           -> To search for courses in current list that are have the professor 'query'\n" +
                        "ID query                  -> To search for courses in current list that have the ID 'query'\n" +
                        "DAY query                 -> To search for courses that meet on a specific day ex. DAY monday\n" +
                        "TIME query                -> To search for courses in current list that meet withing a specific time. The format for query is: startTime-endTime ex. 09:00-16:00\n" +
                        "ADD scheduleName courseID -> To add a specific course to an existing schedule");
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
                        System.out.println("Searching by " + filter + " is not currently supported. Try command HELP");
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
