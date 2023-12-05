package servicecourse.services.bikebrands;

import servicecourse.generated.types.BikeBrand;
import servicecourse.generated.types.CreateBikeBrandInput;
import servicecourse.services.exceptions.BikeBrandAlreadyExistsException;
import servicecourse.services.exceptions.BikeBrandNotFoundException;

import java.util.List;

public interface BikeBrandsService {
    /**
     * @param input the details of the new bike brand
     * @return the newly persisted bike brand, throws otherwise
     * @throws BikeBrandAlreadyExistsException if the bike brand name specified in {@code input}
     *                                         already exists
     */
    BikeBrand createBikeBrand(CreateBikeBrandInput input) throws BikeBrandAlreadyExistsException;

    /**
     * @param name the name of the bike brand to delete
     * @return the name of the deleted bike brand if it existed, throws otherwise
     * @throws BikeBrandNotFoundException if a bike brand with the provided {@code name} doesn't
     *                                    exist
     */
    String deleteBikeBrand(String name) throws BikeBrandNotFoundException;

    /** @return all bike brands */
    List<BikeBrand> bikeBrands();
}
