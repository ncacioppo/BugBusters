package bugbusters;

import java.time.LocalTime;
import java.util.*;

public class Course {
    private int id;
    private String name;
    private String description;
    private String department;
    private int code;
    private Term term;
    private char section;
    private String instructor;
    private Set<MeetingTime> meetingTimes;
    private int credits;

    public Course(int id, String name, String description, String department, int code, Term term,
                   char section, String instructor, Set<MeetingTime> meetingTimes,
                   int credits) {
        setId(id);
        setName(name);
        setDescription(description);
        setDepartment(department);
        setCode(code);
        setTerm(term);
        setSection(section);
        setInstructor(instructor);
        setMeetingTimes(meetingTimes);
        setCredits(credits);
    }

    public Course(ArrayList<String> data) {

        //There are no Course id's in the Excel file. We could make our own maybe in SQL
        setId(0);

        //Course Title is in the 6th column of the Excel file
        setName(data.get(5));

        //There is no description in the Excel file
        setDescription("No protocol determined yet");

        //Department is in the 3rd column of the Excel file
        setDepartment(data.get(2));

        //The course code is in the 4th column of the Excel file
        setCode(Integer.parseInt(data.get(3)));

        //The term code in the 2nd column of the Excel file is 10 for Fall and 30 for Spring
        String season;
        if (Integer.parseInt(data.get(1)) == 10){
            season = "Fall";
        } else {
            season = "Spring";
        }
        //The year of the term is in the 1st column of the Excel file
        setTerm(new Term(season, Integer.parseInt(data.get(0))));


        //Sections is in the 5th column, Section was changed from char to String because section can be empty
        String testSection = (data.get(4));
        if (testSection.length() == 0){
            setSection('\0');
        } else {
            setSection(testSection.charAt(0));
        }

        //First name of the instructor is their preferred name unless there is none, then it is their official first name
        String firstName;
        if (data.size() > 18) {
            if (data.get(18).length() > 0) {
                firstName = data.get(18);
            } else {
                firstName = data.get(17);
            }
        } else {
            firstName = data.get(17);
        }

        //The instructors last name is in the 17th column of the Excel file
        setInstructor(firstName + " " + data.get(16));

        Set<MeetingTime> times = new HashSet<>();

        //Some courses do not have meeting times. This checks if they do before it tries to add any.
        if ((data.get(14).length() > 0)&&(data.get(15).length() > 0)) {

            String data14 = data.get(14);
            String data15 = data.get(15);

            List temp = Arrays.asList(data14.split(" "));
            if (temp.contains("AM")||temp.contains("PM")){
                List tempEnd = Arrays.asList(data15.split(" "));

                String[] time = (data14.split(" ")[0]).split(":");
                if (temp.contains("AM")){
                    data14 = "T " + time[0] + ":" + time[1];
                } else {
                    if (!time[0].equalsIgnoreCase("12")) {
                        data14 = "T " + (Integer.parseInt(time[0]) + 12) + ":" + time[1];
                    } else {
                        data14 = "T " + time[0] + ":" + time[1];
                    }
                }

                time = (data15.split(" ")[0]).split(":");
                if (tempEnd.contains("AM")){
                    data15 = "T " + time[0] + ":" + time[1];
                } else {
                    if (!time[0].equalsIgnoreCase("12")) {
                        data15 = "T " + (Integer.parseInt(time[0]) + 12) + ":" + time[1];
                    } else {
                        data15 = "T " + time[0] + ":" + time[1];
                    }
                }

            }

            String startTime = (data14.split(" "))[1];
            int startHour = Integer.parseInt((startTime.split(":"))[0]);
            int startMinute = Integer.parseInt((startTime.split(":"))[1]);
            LocalTime start = LocalTime.of(startHour, startMinute);

            String endTime = (data15.split(" "))[1];
            int endHour = Integer.parseInt((endTime.split(":"))[0]);
            int endMinute = Integer.parseInt((endTime.split(":"))[1]);
            LocalTime end = LocalTime.of(endHour, endMinute);

            for (int i = 10; i <= 14; i++) {
                if (data.get(i).length() > 0) {
                    times.add(new MeetingTime(toDay(data.get(i)), start, end));
                }
            }


            setMeetingTimes(times);

            //Credit hours are in the 7th column of the Excel files
            setCredits(Integer.parseInt(data.get(6)));
        }

    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    private void setDescription(String description){
        this.description = description;
    }

    public String getDepartment() {
        return department;
    }

    private void setDepartment(String department) {
        this.department = department;
    }

    public int getCode() {
        return code;
    }

    private void setCode(int code) {
        this.code = code;
    }

    public Term getTerm() {
        return term;
    }

    private void setTerm(Term term) {
        this.term = term;
    }

    public char getSection() {
        return section;
    }

    private void setSection(char section) {
        this.section = section;
    }

    public String getInstructor() {
        return instructor;
    }

    private void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Set<MeetingTime> getMeetingTimes() {
        return meetingTimes;
    }

    private void setMeetingTimes(Set<MeetingTime> meetingTimes) {
        this.meetingTimes = meetingTimes;
    }

    public int getCredits() {
        return credits;
    }

    private void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * This function is just to help reading an Excel file and
     * converting its data into courses
     * @param dayChar character from Excel representing a day
     * @return the actual name of the day
     */
    private Day toDay(String dayChar){
        Day out;

        switch (dayChar) {
            case "M":
                out = Day.MONDAY;
                break;
            case "T":
                out = Day.TUESDAY;
                break;
            case "W":
                out = Day.WEDNESDAY;
                break;
            case "R":
                out = Day.THURSDAY;
                break;
            case "F":
                out = Day.FRIDAY;
                break;
            default:
                out = Day.NONE;
        }

        return out;
    }

    /**
     * This is not case-sensitive for any String member variables of Course.
     * @param other
     * @return true if other is equivalent to this and false if not
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Course)){
            return false;
        }

        Course o = (Course) other;
        boolean meetingTest = false;
        boolean term = false;

        if (this.id == o.id){
            if (this.name.equalsIgnoreCase(o.name)){
                if (this.department.equalsIgnoreCase(o.department)){
                    if (this.code == o.code){
                        if (this.term.equals(o.term)){
                            if (this.section == o.section){
                                if (this.instructor.equalsIgnoreCase(o.instructor)){
                                    if (this.credits == o.credits){
                                        if ((this.meetingTimes == null) && (o.meetingTimes == null)){
                                            meetingTest = true;
                                        } else if ((this.meetingTimes != null) && (o.meetingTimes != null)){
                                            meetingTest = true;
                                            for (MeetingTime meetingTime : this.meetingTimes){
                                                boolean found = false;
                                                for (MeetingTime otherMeetingTime : o.meetingTimes){
                                                    if (meetingTime.equals(otherMeetingTime)){
                                                        found = true;
                                                    }
                                                }
                                                if (!found){
                                                    meetingTest = false;
                                                }
                                            }
                                        }
                                        return meetingTest;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * This depends heavily on the toString method of MeetinfTime
     * @return A string representation of a course.
     */
    public String toString() {
        String out = "ID: " + id + "\n" +
                "Name: " + name + "\n" +
                "Department: " + department + "\n" +
                "Code: " + code + "\n" +
                "Term: " + term + "\n" +
                "Section: " + section + "\n" +
                "Instructor: " + instructor + "\n" +
                "Meeting Times: " + meetingTimes + "\n" +
                "Credits: " + credits + "\n" +
                "Description: " + description + "\n";

        return out;
    }

}
