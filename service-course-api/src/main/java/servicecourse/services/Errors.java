package servicecourse.services;

import java.util.NoSuchElementException;

public class Errors {
    public static RuntimeException newModelNotFoundError() {
        return new NoSuchElementException("Model not found");
    }

    public static RuntimeException newGroupsetNotFoundError() {
        return new NoSuchElementException("Groupset not found");
    }

    public static RuntimeException newBikeNotFoundError() {
        return new NoSuchElementException("Bike not found");
    }

    public static RuntimeException newBikeBrandNotFoundError() {
        return new NoSuchElementException("Bike brand not found");
    }

    public static RuntimeException newBikeBrandAlreadyExistsError() {
        return new IllegalArgumentException("Bike brand already exists");
    }

    /** No fields specified on StringFilterInput, must specify at least one */
    public static RuntimeException emptyStringFilterInput() {
        return new IllegalStateException(
                "No fields specified on StringFilterInput, must specify at least one");
    }
}
