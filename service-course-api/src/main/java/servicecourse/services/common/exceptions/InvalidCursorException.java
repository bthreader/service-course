package servicecourse.services.common.exceptions;

import servicecourse.services.common.CursorName;

public class InvalidCursorException extends IllegalArgumentException {
    public InvalidCursorException(CursorName cursorName, String cursorValue) {
        super("Invalid cursor value provided for " + cursorName.getName() + ": " + cursorValue);
    }
}
