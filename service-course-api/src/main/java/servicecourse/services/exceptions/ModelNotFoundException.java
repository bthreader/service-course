package servicecourse.services.exceptions;

import java.util.NoSuchElementException;

public class ModelNotFoundException extends NoSuchElementException {
    public ModelNotFoundException(String modelId) {
        super("Model with ID '" + modelId + "' not found");
    }
}
