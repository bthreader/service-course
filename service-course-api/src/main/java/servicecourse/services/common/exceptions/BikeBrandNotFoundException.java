package servicecourse.services.common.exceptions;

import java.util.NoSuchElementException;

public class BikeBrandNotFoundException extends NoSuchElementException {
    public BikeBrandNotFoundException(String brandName) {
        super("Bike brand with name '" + brandName + "' not found");
    }
}
