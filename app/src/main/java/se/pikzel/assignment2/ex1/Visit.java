package se.pikzel.assignment2.ex1;

public class Visit {
    public static String ID = "id";
    public static String YEAR = "year";
    public static String COUNTRY = "country";
    public static String POSITION = "position";

    private long id;
    private String country;
    private int year;

    public Visit() {}

    public Visit(int year, String country) {
        this.country = country;
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "" + year + " " + country;
    }
}
