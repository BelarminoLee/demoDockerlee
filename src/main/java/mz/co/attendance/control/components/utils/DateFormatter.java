package mz.co.attendance.control.components.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * @author Diogo.Amaral
 */
public class DateFormatter {

    public static String date(Date date) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return date != null ? format.format(date) +"h" : "";
    }

    public static String convertTimeToString(Long time) {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(time);
    }

    public static Date asDate(LocalDate localDate) {
        return Objects.nonNull(localDate) ?Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    public static Date asDate(LocalDateTime localDate) {
        return Objects.nonNull(localDate) ?Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static long asLongDate(Date specificDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(specificDate);
        cal.add(Calendar.HOUR, LocalDateTime.now().getHour());
        cal.add(Calendar.MINUTE, LocalDateTime.now().getMinute());
        cal.add(Calendar.SECOND, LocalDateTime.now().getSecond());
        return cal.getTime().getTime();
    }
}
