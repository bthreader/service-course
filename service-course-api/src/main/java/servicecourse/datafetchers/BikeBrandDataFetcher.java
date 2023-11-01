package servicecourse.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import servicecourse.generated.types.BikeBrand;
import servicecourse.generated.types.Model;
import servicecourse.services.models.ModelsService;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class BikeBrandDataFetcher {
    private final ModelsService modelsService;

    /**
     * This is an enhanced attribute. Services will not generate it ahead of time, unlike brand
     * name.
     */
    @DgsData(parentType = "BikeBrand", field = "models")
    public List<Model> models(DgsDataFetchingEnvironment dfe) {
        BikeBrand bikeBrand = dfe.getSource();
        return modelsService.findByBrandName(bikeBrand.getName());
    }
}
