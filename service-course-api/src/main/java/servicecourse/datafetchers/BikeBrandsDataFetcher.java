package servicecourse.datafetchers;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import servicecourse.generated.types.BikeBrand;
import servicecourse.generated.types.CreateBikeBrandInput;
import servicecourse.generated.types.Model;
import servicecourse.services.bikebrands.BikeBrandsService;
import servicecourse.services.models.ModelsService;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class BikeBrandsDataFetcher {
    private final ModelsService modelsService;
    private final BikeBrandsService bikeBrandsService;

    /**
     * This is an enhanced attribute. Services will not generate it ahead of time, unlike brand
     * name. Therefore, if specified, it must be computed here.
     */
    @DgsData(parentType = "BikeBrand", field = "models")
    public List<Model> models(DgsDataFetchingEnvironment dfe) {
        BikeBrand bikeBrand = dfe.getSource();
        // Data loader here
        // Why?
        // Because if some wind up merchant decides to ask for models on all bike brands
        // after running bikeBrands query
        // Models service will get pinged N+1 times
        return modelsService.findByBrandName(bikeBrand.getName());
    }

    @DgsQuery
    public List<BikeBrand> bikeBrands() {
        return bikeBrandsService.bikeBrands();
    }

    @DgsMutation
    public BikeBrand createBikeBrand(@InputArgument CreateBikeBrandInput input) {
        return bikeBrandsService.createBikeBrand(input);
    }

    @DgsMutation
    public String deleteBikeBrand(@InputArgument String name) {
        return bikeBrandsService.deleteBikeBrand(name);
    }
}
