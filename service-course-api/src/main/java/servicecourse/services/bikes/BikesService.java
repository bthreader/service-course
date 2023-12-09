package servicecourse.services.bikes;

import servicecourse.generated.types.Bike;
import servicecourse.generated.types.BikesFilterInput;
import servicecourse.generated.types.CreateBikeInput;
import servicecourse.generated.types.UpdateBikeInput;
import servicecourse.services.exceptions.BikeNotFoundException;
import servicecourse.services.exceptions.GroupsetNotFoundException;
import servicecourse.services.exceptions.ModelNotFoundException;

import java.util.List;

public interface BikesService {
    List<Bike> bikes(BikesFilterInput filter);

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
