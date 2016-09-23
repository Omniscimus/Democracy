package net.omniscimus.bukkit.democracy;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class can put a date in a specific format which is useful for the
 * configuration file.
 */
public class DateFormatter {

    private final Calendar calendar;

    /**
     * Creates a new DateFormatter instance.
     * 
     * @param date
     *            The date to format.
     */
    public DateFormatter(Date date) {
        calendar = new GregorianCalendar();
        calendar.setTime(date);
    }

    /**
     * Gets the week number.
     * 
     * @return the week number.
     */
    public int getWeekNumber() {
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Gets the year.
     * 
     * @return the year.
     */
    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

}
