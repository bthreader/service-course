package servicecourse.services.exceptions;

public class EmptyStringFilterInputException extends IllegalArgumentException {
    public EmptyStringFilterInputException(String fieldPath) {
        super("No fields specified on the StringFilterInput for path '" + fieldPath
                      + "', please specify at least one");
    }
}
