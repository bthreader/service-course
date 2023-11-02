package servicecourse.services.models;

import servicecourse.generated.types.CreateModelInput;
import servicecourse.generated.types.Model;

import java.util.List;
import java.util.Map;

public interface ModelsService {
    List<Model> findByBrandName(String brandName);

    Model createModel(CreateModelInput input);

    String deleteModel(String id);

    Map<String, List<Model>> modelsForBikeBrands(List<String> brandNames);
}
