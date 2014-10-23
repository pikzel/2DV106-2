package se.pikzel.assignment2.ex3;

import java.io.Serializable;

/**
 * @author Pontus Palmenäs
 */
public class Alarm implements Serializable {
    private int id;
    private int hour;
    private int minute;

    public Alarm(int id, int hour, int minute) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String toString() {
        return "" + pad(hour) + ":" + pad(minute);
    }

    public static String pad(int n) {
        if (n == 0) return "00";
        if (n / 10 == 0) return "0" + n;
        return String.valueOf(n);
    }

    public int getId() {
        return id;
    }
}
