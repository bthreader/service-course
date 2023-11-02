package servicecourse.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import servicecourse.generated.types.CreateModelInput;
import servicecourse.generated.types.Model;
import servicecourse.services.models.ModelsService;

@DgsComponent
@RequiredArgsConstructor
public class ModelsDataFetcher {
    private final ModelsService modelsService;

    @DgsMutation
    public Model createModel(@InputArgument CreateModelInput input) {
        return modelsService.createModel(input);
    }

    @DgsMutation
    public String deleteModel(@InputArgument String id) {
        return modelsService.deleteModel(id);
    }
}
