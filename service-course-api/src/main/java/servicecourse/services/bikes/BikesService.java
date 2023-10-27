package servicecourse.services.bikes;

import servicecourse.generated.types.Bike;
import servicecourse.generated.types.BikesFilterInput;
import servicecourse.generated.types.CreateBikeInput;
import servicecourse.generated.types.UpdateBikeInput;

import java.util.List;

public interface BikesService {
    List<Bike> bikes(BikesFilterInput bikesFilterInput);

    /** @return the newly persisted bike if successful, throws otherwise */
    Bike createBike(CreateBikeInput createBikeInput);

    /** @return the updated bike if successful, throws otherwise */
    Bike updateBike(UpdateBikeInput updateBikeInput);

    /** @return the ID of the deleted bike if it existed, throws otherwise */
    Long deleteBike(String id);
}
