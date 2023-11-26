package servicecourse.services.models;

import servicecourse.generated.types.CreateModelInput;
import servicecourse.generated.types.Model;

import java.util.List;
import java.util.Map;

public interface ModelsService {
    /**
     * @return all models belonging to the brand
     */
    List<Model> findByBrandName(String brandName);

    Model createModel(CreateModelInput input);

    String deleteModel(String id);

    /**
     * @return a map; bike brand name -> models for that brand
     */
    Map<String, List<Model>> modelsForBikeBrands(List<String> brandNames);
}
