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
}
