package bugbusters;

import java.util.Scanner;

public class Term implements Comparable<Term> {
    // I will implement this in the next few days.
    private String season;
    private int year;

    public Term(String season, int year) {
        this.season = season;
        this.year = year;
    }

    public Term(String term) {
        Scanner scn = new Scanner(term);
        this.season = scn.next();
        this.year = scn.nextInt();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Term)){
            return false;
        }
        Term other = (Term) o;
        return (this.season.equals(other.season)) && (this.year == other.year);
    }

    @Override
    // Format: SPRING 2024
    public String toString() {
        return season + " " + year;
    }

    @Override
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
