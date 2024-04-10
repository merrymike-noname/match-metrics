package com.matchmetrics.entity.validator;

import com.matchmetrics.exception.DateConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class DateValidator {
    private final Logger logger = LoggerFactory.getLogger(DateValidator.class);

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
}
