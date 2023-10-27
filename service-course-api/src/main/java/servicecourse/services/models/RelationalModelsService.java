package servicecourse.services.models;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import servicecourse.generated.types.Model;
import servicecourse.repo.ModelEntity;
import servicecourse.repo.ModelRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelationalModelsService implements ModelsService {
    private final ModelRepository modelRepository;

    @Override
    public List<Model> findByBrandName(String brandName) {
        return modelRepository.findAllByBrandName(brandName)
                .stream()
                .map(ModelEntity::asModel)
                .collect(Collectors.toList());
    }
}
