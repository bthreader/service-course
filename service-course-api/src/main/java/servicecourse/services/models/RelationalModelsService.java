package servicecourse.services.models;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import servicecourse.generated.types.CreateModelInput;
import servicecourse.generated.types.Model;
import servicecourse.repo.BikeBrandRepository;
import servicecourse.repo.ModelEntity;
import servicecourse.repo.ModelRepository;
import servicecourse.services.Errors;

import java.util.List;
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

        // TODO Write an apply method with @notnulls

        return null;
    }

    @Override
    public Long deleteModel(String id) {
        return modelRepository.findById(ModelId.deserialize(id))
                .map((entity) -> {
                    modelRepository.deleteById(entity.getId());
                    return entity.getId();
                })
                .orElseThrow(Errors::newModelNotFoundError);
    }
}

