package servicecourse.services.models;

import servicecourse.generated.types.CreateModelInput;
import servicecourse.generated.types.Model;
import servicecourse.services.exceptions.BikeBrandNotFoundException;
import servicecourse.services.exceptions.ModelNotFoundException;

import java.util.List;
import java.util.Map;

public interface ModelsService {
    /**
     * @return all models belonging to the brand
     */
    List<Model> findByBrandName(String brandName);

    /**
     * @param input the details of the new model
     * @return the newly persisted model
     * @throws BikeBrandNotFoundException if the bike brand specified in {@code input} doesn't
     *                                    exist
     */
    Model createModel(CreateModelInput input);

    /**
     * @param id the ID of the model to delete
     * @return the ID of the deleted model
     * @throws ModelNotFoundException if a model with the provided {@code id} doesn't exist
     */
    String deleteModel(String id);

    /**
     * @return a map; bike brand name -> models for that brand
     */
    Map<String, List<Model>> modelsForBikeBrands(List<String> brandNames);
}
