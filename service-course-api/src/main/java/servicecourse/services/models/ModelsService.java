package servicecourse.services.models;

import servicecourse.generated.types.CreateModelInput;
import servicecourse.generated.types.Model;

import java.util.List;

public interface ModelsService {
    List<Model> findByBrandName(String brandName);

    Model createModel(CreateModelInput input);

    Long deleteModel(String id);
}
