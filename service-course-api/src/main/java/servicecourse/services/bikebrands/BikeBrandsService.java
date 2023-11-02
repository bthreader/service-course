package servicecourse.services.bikebrands;

import servicecourse.generated.types.BikeBrand;
import servicecourse.generated.types.CreateBikeBrandInput;

import java.util.List;

public interface BikeBrandsService {
    /** @return the newly persisted bike brand */
    BikeBrand createBikeBrand(CreateBikeBrandInput input);

    /** @return the name of the deleted bike brand if it existed, throws otherwise */
    String deleteBikeBrand(String name);

    /** @return all bike brands */
    List<BikeBrand> bikeBrands();
}
