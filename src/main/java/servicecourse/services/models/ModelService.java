package servicecourse.services.models;

import servicecourse.generated.types.Model;

import java.util.List;

public interface ModelService {
    List<Model> findByBrandName(String brandName);
}
