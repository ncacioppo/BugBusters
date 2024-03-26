package bugbusters;

import java.util.ArrayList;
import java.util.Scanner;

public class Run {

    private static final String terminalString = "Grove City College Schedule Maker -> ";
    private static ArrayList<Course> courses;
    private static Search search;
    private static Scanner scanner;
    public static void run(){
        search = new Search();
        courses = new ArrayList<>(search.getAllCoursesFromExcel());
        scanner = new Scanner(System.in);

        while (true){
            System.out.print(terminalString);

            String input = scanner.nextLine().toUpperCase();

            switch(input) {
                case "SEARCH":
                    runSearch();
                    break;
                case "EXIT":
                    System.exit(0);
                    break;
                default:
                    System.out.println("'" + input + "' is not a recognized command.");
            }
        }
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
