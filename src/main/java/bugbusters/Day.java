package bugbusters;

public enum Day{
    SUNDAY("SUNDAY"),
    MONDAY("MONDAY"),
    TUESDAY("TUESDAY"),
    WEDNESDAY("WEDNESDAY"),
    THURSDAY("THURSDAY"),
    FRIDAY("FRIDAY"),
    SATURDAY("SATURDAY"),
    NONE("NONE");


    private final String name;
    Day(String name) {
        this.name = name.toUpperCase();
    }

    @Override
    public String toString() { return name;}

    public String getAbbrev() {
        if (this.equals(MONDAY)) {
            return "M";
        } else if (this.equals(TUESDAY)) {
            return "T";
        } else if (this.equals(WEDNESDAY)) {
            return "W";
        } else if (this.equals(THURSDAY)) {
            return "R";
        } else if (this.equals(FRIDAY)) {
            return "F";
        } else if (this.equals(SATURDAY)) {
            return "S";
        } else {
            // Sunday
            return "U";
        }
    }

}
