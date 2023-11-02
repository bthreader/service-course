package servicecourse.services.models;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import servicecourse.generated.types.BikeBrand;
import servicecourse.generated.types.CreateModelInput;
import servicecourse.generated.types.Model;
import servicecourse.repo.BikeBrandRepository;
import servicecourse.repo.ModelEntity;
import servicecourse.repo.ModelRepository;
import servicecourse.services.Errors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelationalModelsService implements ModelsService {
    private final ModelRepository modelRepository;
    private final BikeBrandRepository bikeBrandRepository;

    @Override
    public List<Model> findByBrandName(String brandName) {
        return modelRepository.findAllByBrandName(brandName)
                .stream()
                .map(ModelEntity::asModel)
                .collect(Collectors.toList());
    }

    @Override
    public Model createModel(CreateModelInput input) {
        // Validate the brand already exists
        bikeBrandRepository.findById(input.getBrandName())
                .orElseThrow(Errors::newBikeBrandNotFoundError);

        ModelEntity newModel = ModelEntity.builder()
                .name(input.getName())
                .modelYear(input.getModelYear())
                .brandName(input.getBrandName())
                .build();

        return modelRepository.save(newModel).asModel();
    }

    @Override
    public String deleteModel(String id) {
        return modelRepository.findById(ModelId.deserialize(id))
                .map((entity) -> {
                    modelRepository.deleteById(entity.getId());
                    return ModelId.serialize(entity.getId());
                })
                .orElseThrow(Errors::newModelNotFoundError);
    }

    public Map<String, List<Model>> modelsForBikeBrands(List<String> brandNames) {
        Map<String, List<Model>> result = new HashMap<>();
        result.put("hello", List.of(Model.newBuilder()
                                            .name("hello")
                                            .brand(BikeBrand.newBuilder()
                                                           .name("hello").build())
                                            .modelYear(2022)
                                            .build()));
        return result;
    }
}

