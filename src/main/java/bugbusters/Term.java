package bugbusters;

import java.util.Scanner;

public class Term {
    // I will implement this in the next few days.
    private String season;
    private int year;

    public Term(String season, int year) {
        setSeason(season);
        setYear(year);
    }

    public Term(String term) {
        Scanner scn = new Scanner(term);
        setSeason(scn.next());
        setYear(scn.nextInt());
    }

    public String getSeason(){
        return this.season;
    }
    private void setSeason(String season){
        this.season = season;
    }

    public int getYear(){
        return this.year;
    }
    private void setYear(int year){
        this.year = year;
    }

    public boolean equals(Term other) {
        return (this.season.equalsIgnoreCase(other.season)) && (this.year == other.year);
    }

    // Format: SPRING 2024
    public String toString() {
        return season + " " + year;
    }


    public int compareTo(Term other) {
        if ((other.year < this.year) ||
                ((other.year == this.year) &&
                        (other.season.equals("SPRING") && this.season.equals("FALL")))) {
            return 1;
        } else if (this.equals(other)) {
            return 0;
        } else return -1;

    }

}
