package servicecourse.services.common.exceptions;

import java.util.NoSuchElementException;

public class BikeNotFoundException extends NoSuchElementException {
    public BikeNotFoundException(String bikeId) {
        super(("Bike with ID '" + bikeId + "' not found"));
    }
}
