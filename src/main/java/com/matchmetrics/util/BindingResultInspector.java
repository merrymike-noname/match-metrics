package com.matchmetrics.util;

import com.matchmetrics.exception.InvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BindingResultInspector {

    private final Logger logger = LoggerFactory.getLogger(BindingResultInspector.class);

    public void checkBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            String errorMessage = String.join(", ", errorMessages);
            logger.error("Error occurred while creating team: {}", errorMessage);
            throw new InvalidDataException(errorMessage);
        }
    }
}
