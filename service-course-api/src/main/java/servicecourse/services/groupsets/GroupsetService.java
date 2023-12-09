package servicecourse.services.groupsets;

import servicecourse.generated.types.CreateGroupsetInput;
import servicecourse.generated.types.Groupset;
import servicecourse.services.exceptions.GroupsetAlreadyExistsException;
import servicecourse.services.exceptions.GroupsetNotFoundException;

public interface GroupsetService {
    /**
     * @param input the details of the new groupset
     * @return the newly persisted groupset
     * @throws GroupsetAlreadyExistsException if the groupset name specified in {@code input}
     *                                        already exists
     */
    Groupset createGroupset(CreateGroupsetInput input);

    /**
     * @param name the name of the groupset to delete
     * @return the name of the deleted groupset
     * @throws GroupsetNotFoundException if a groupset with the provided {@code name} doesn't exist
     */
    String deleteGroupset(String name);
}
