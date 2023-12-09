package servicecourse.services.exceptions;

public class GroupsetAlreadyExistsException extends IllegalArgumentException {
    public GroupsetAlreadyExistsException(String groupsetName) {
        super("A groupset with the name '" + groupsetName + "' already exists");
    }
}
