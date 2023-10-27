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
     * The {@code name} attribute will already be populated by the queries that return
     * {@code BikeBrand}. However, these queries won't load models related to the brand, so we need
     * to resolve this separately.
     */
    @DgsData(parentType = "BikeBrand", field = "models")
    public List<Model> models(DgsDataFetchingEnvironment dfe) {
        BikeBrand bikeBrand = dfe.getSource();
        return modelsService.findByBrandName(bikeBrand.getName());
    }
}
