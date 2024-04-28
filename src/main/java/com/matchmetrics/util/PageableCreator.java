package com.matchmetrics.util;

import com.matchmetrics.exception.FieldDoesNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PageableCreator {
    private final Logger logger = LoggerFactory.getLogger(PageableCreator.class);

    public Pageable createPageable(int page, int perPage, String sortBy, Class<?> clazz) {
        if (!sortBy.equals("default") && !checkIfFieldExists(sortBy, clazz)) {
            logger.error("Error occurred while trying to find a '{}' field in {}", sortBy, clazz.getName());
            throw new FieldDoesNotExistException(sortBy, clazz);
        }
        Sort sort = sortBy.equals("default") ? Sort.unsorted() : Sort.by(sortBy);
        return PageRequest.of(page, perPage, sort);
    }

    private boolean checkIfFieldExists(String fieldName, Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .anyMatch(field -> field.getName().equals(fieldName));
    }
}
