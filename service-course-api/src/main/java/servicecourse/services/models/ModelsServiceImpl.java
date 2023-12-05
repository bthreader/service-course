package servicecourse.services.models;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import servicecourse.generated.types.CreateModelInput;
import servicecourse.generated.types.Model;
import servicecourse.repo.BikeBrandRepository;
import servicecourse.repo.ModelEntity;
import servicecourse.repo.ModelRepository;
import servicecourse.services.exceptions.BikeBrandNotFoundException;
import servicecourse.services.exceptions.ModelNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelsServiceImpl implements ModelsService {
    private final ModelRepository modelRepository;
    private final BikeBrandRepository bikeBrandRepository;

    @Override
    public List<Model> findByBrandName(String brandName) {
        return modelRepository.findByBrandName(brandName)
                .stream()
                .map(ModelEntity::asModel)
                .collect(Collectors.toList());
    }

    @Override
    public Model createModel(CreateModelInput input) {
        // Validate the brand already exists
        bikeBrandRepository.findById(input.getBrandName())
                .orElseThrow(() -> new BikeBrandNotFoundException(input.getBrandName()));

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
                .orElseThrow(() -> new ModelNotFoundException(id));
    }

    public Map<String, List<Model>> modelsForBikeBrands(List<String> brandNames) {
        return modelRepository.findByBrandNameIn(brandNames)
                .stream()
                .map(ModelEntity::asModel)
                .collect(Collectors.groupingBy(m -> m.getBrand().getName()));
    }
}

