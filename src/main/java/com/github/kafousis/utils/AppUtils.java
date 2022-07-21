package com.github.kafousis.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AppUtils {

    public static String TIMEZONE;
    public static String DATE_FORMAT;

    @Value("${spring.jackson.time-zone}")
    public void setTimezone(String timezone) {
        TIMEZONE = timezone;
    }

    @Value("${spring.jackson.date-format}")
    public void setDateFormat(String dateFormat) {
        DATE_FORMAT = dateFormat;
    }

    public static String getCurrentTimestamp(){
        ZonedDateTime zonedDateTimeNow = ZonedDateTime.now(ZoneId.of(TIMEZONE));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String formattedDateTime = zonedDateTimeNow.format(formatter);
        return formattedDateTime;
    }
}
