package servicecourse.services.common.exceptions;

public class BikeBrandAlreadyExistsException extends IllegalArgumentException {
    public BikeBrandAlreadyExistsException(String brandName) {
        super("A bike brand with the name '" + brandName + "' already exists");
    }
}
