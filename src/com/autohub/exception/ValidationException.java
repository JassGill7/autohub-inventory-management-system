
package com.autohub.exception;

import java.util.List;

public class ValidationException extends RuntimeException {

    private final List<String> errors;

    public ValidationException(List<String> errors) {

        super("Car validation failed: "
                + String.join("; ", errors));

        this.errors = List.copyOf(errors);
    }

    public List<String> getErrors() {
        return errors;
    }
}
