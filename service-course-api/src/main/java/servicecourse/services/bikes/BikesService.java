package servicecourse.services.bikes;

import org.springframework.lang.Nullable;
import servicecourse.generated.types.*;
import servicecourse.services.common.exceptions.BikeNotFoundException;
import servicecourse.services.common.exceptions.GroupsetNotFoundException;
import servicecourse.services.common.exceptions.ModelNotFoundException;

public interface BikesService {
    /**
     * @param filter the filter to apply to the bikes
     * @param first  the number of results to return. Must be greater than zero. The maximum value
     *               is 100, values higher than this will be truncated.
     * @param after  if specified, only return results after the cursor
     * @return a connection of bikes
     * @throws IllegalArgumentException if first is less than one
     */
    BikeConnection bikes(@Nullable BikesFilterInput filter, int first, @Nullable CursorInput after);

    /**
     * @param input the details of the new bike
     * @return the newly persisted bike
     * @throws ModelNotFoundException    if the model specified in {@code input} doesn't exist
     * @throws GroupsetNotFoundException if the groupset specified in {@code input} doesn't exist
     */
    Bike createBike(CreateBikeInput input);

    /**
     * @param input the details of an update to a bike
     * @return the updated bike
     * @throws BikeNotFoundException     if the bike ID provided in {@code input} doesn't exist
     * @throws GroupsetNotFoundException if a groupset is specified in {@code input} but it doesn't
     *                                   exist
     */
    Bike updateBike(UpdateBikeInput input);

    /**
     * @param id the ID of the bike to delete
     * @return the ID of the deleted bike
     * @throws BikeNotFoundException if a bike with the provided {@code id} doesn't exist
     */
    Long deleteBike(String id);
}
