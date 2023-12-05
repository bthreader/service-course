package servicecourse.services.exceptions;

import java.util.NoSuchElementException;

public class GroupsetNotFoundException extends NoSuchElementException {
    public GroupsetNotFoundException(String groupsetName) {
        super("Groupset with name '" + groupsetName + "' not found");
    }
}
