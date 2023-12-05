package servicecourse.services.exceptions;

public class BikeBrandAlreadyExistsException extends IllegalArgumentException {
    public BikeBrandAlreadyExistsException(String brandName) {
        super("Bike brand with name '" + brandName + "' already exists");
    }
}
