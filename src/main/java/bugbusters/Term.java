package bugbusters;

import java.util.Scanner;

public class Term {
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

    public boolean equals(Object other) {
        if (!(other instanceof Term t)) {
            return false;
        }
        if (!(season.equals(t.season))) {
            return false;
        } else if (!(year == t.year)) {
            return false;
        }
        return true;
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
