package servicecourse.datafetchers;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import servicecourse.generated.DgsConstants;
import servicecourse.generated.types.BikeBrand;
import servicecourse.generated.types.CreateBikeBrandInput;
import servicecourse.generated.types.Model;
import servicecourse.services.bikebrands.BikeBrandsService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class BikeBrandsDataFetcher {
    private final BikeBrandsService bikeBrandsService;

    /**
     * This is an enhanced attribute. Services will not generate it ahead of time. Therefore, if
     * requested by the user, it must be computed here.
     * <p>
     * A data loader is used to avoid sending multiple separate requests to the models service when
     * handling a request that involves multiple bike brands (see {@link #bikeBrands()}).
     */
    @DgsData(parentType = DgsConstants.BIKEBRAND.TYPE_NAME, field = DgsConstants.BIKEBRAND.Models)
    public CompletableFuture<List<Model>> models(DgsDataFetchingEnvironment dfe) {
        BikeBrand bikeBrand = dfe.getSource();
        DataLoader<String, List<Model>> modelsDataLoader = dfe.getDataLoader("models");
        return modelsDataLoader.load(bikeBrand.getName());
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
