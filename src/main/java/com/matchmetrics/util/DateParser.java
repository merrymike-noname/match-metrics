package com.matchmetrics.util;

import com.matchmetrics.exception.DateConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateParser {
    private final Logger logger = LoggerFactory.getLogger(DateParser.class);

    public void validate(String date) {
        if (date == null || date.isEmpty()) {
            throw new DateConversionException("Date cannot be null or empty");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
        } catch (ParseException e) {
            logger.error("Failed to parse date '{}'", date);
            throw new DateConversionException(e);
        }
    }

    public Date convertStringToDate(String strDate) {
        if (strDate == null || strDate.isEmpty()) {
            throw new DateConversionException("Date cannot be null or empty");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            return sdf.parse(strDate);
        } catch (ParseException e) {
            logger.error("Error occurred while converting string to date: {}", e.getMessage());
            throw new DateConversionException(e);
        }
    }
}
