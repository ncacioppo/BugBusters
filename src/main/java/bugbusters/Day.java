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

}
