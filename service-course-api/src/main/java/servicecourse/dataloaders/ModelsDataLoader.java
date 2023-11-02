package servicecourse.dataloaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;
import servicecourse.generated.types.Model;
import servicecourse.services.models.ModelsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "models")
@RequiredArgsConstructor
public class ModelsDataLoader implements MappedBatchLoader<String, List<Model>> {
    private final ModelsService modelsService;

    @Override
    public CompletionStage<Map<String, List<Model>>> load(Set<String> brandNames) {
        return CompletableFuture.supplyAsync(() -> modelsService.modelsForBikeBrands(new ArrayList<>(
                brandNames)));
    }
}
