package bugbusters;

import java.util.Set;

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
                   int credit) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.department = department;
        this.code = code;
        this.term = term;
        this.section = section;
        this.instructor = instructor;
        this.meetingTimes = meetingTimes;
        this.credits = credit;
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

    public boolean equals(Course other) {
        return false;
    }

    public String toString() {
        return null;
    }

}
