package zutils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormatSymbols;


import static java.lang.String.format;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public final class StringUtils {
    private static final SimpleDateFormat incomingSdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat outgoingSdf = new SimpleDateFormat("MMM d yyyy");

    private static final DateFormatSymbols outgoingMonth = new DateFormatSymbols();

    public static String getFilterHeader(String column) {

        return String.format("Filter by %s", column);
    }

    public static String parsedDate(String date) {

        try {
            Date formattedDate = incomingSdf.parse(date);

            return outgoingSdf.format(formattedDate);
        } catch (ParseException e) {

            throw new RuntimeException(e);
        }
    }

    public static String getDateAsString(ZonedDateTime date) {

        return  LocalDate.of(date.getYear(), date.getMonth(),
                date.getDayOfMonth()).format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }

    public static String getDateAsString(ZonedDateTime date, String format) {

        return  LocalDate.of(date.getYear(), date.getMonth(),
                date.getDayOfMonth()).format(DateTimeFormatter.ofPattern(format));
    }

    public static String getStringMonth(int month){

        return outgoingMonth.getShortMonths()[month-1];
    }

    public static String getStringMonthYear(Month month, int year){

        return   format("%s %s", month.getDisplayName(TextStyle.FULL, Locale.US), year);
    }

    public static String getStringForDate(ZonedDateTime currentDate){

        return format("%s %s",
                currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.US), currentDate.getYear());
    }

    public static String getDateAsString(int year, Month month, int day){

        return LocalDate.of(year, month, day).format(DateTimeFormatter.ofPattern(day<10?"MMM d yyyy":"MMM dd yyyy"));
    }

    public static String getLastDayOfTheMonthDateAsString(int year, Month month){

        return  LocalDate.of(year, month, 1).with(lastDayOfMonth()).format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
    }

    public static String getLastDayOfMonth(int year, Month month){

        return String.valueOf(LocalDate.of(year, month, 1).with(lastDayOfMonth()).getDayOfMonth());
    }

}

