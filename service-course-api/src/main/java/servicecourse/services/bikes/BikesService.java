package servicecourse.services.bikes;

import servicecourse.generated.types.*;

import java.util.List;
import java.util.Optional;

public interface BikesService {
    List<Bike> bikes(BikesFilterInput bikesFilterInput);

    /** @return the newly persisted bike if successful */
    Bike createBike(CreateBikeInput createBikeInput);

    /** @return the updated bike if it exists, empty otherwise */
    Bike updateBike(UpdateBikeInput updateBikeInput);

    /** @return the ID of the deleted bike if it existed, empty otherwise */
    Optional<Long> deleteBike(DeleteBikeInput deleteBikeInput);
}
