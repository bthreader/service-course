package servicecourse.services.bikes;

import lombok.Builder;
import servicecourse.repo.GroupsetEntity;
import servicecourse.repo.ModelEntity;

import java.net.URL;
import java.util.Optional;

@Builder
public class CreateBikeParams implements CrudBikeInput {
    private ModelEntity modelEntity;
    private GroupsetEntity groupsetEntity;
    private URL heroImageUrl;
    private String size;

    @Override
    public Optional<ModelEntity> model() {
        return Optional.of(modelEntity);
    }

    @Override
    public Optional<GroupsetEntity> groupset() {
        return Optional.of(groupsetEntity);
    }

    @Override
    public Optional<URL> heroImageUrl() {
        return Optional.of(heroImageUrl);
    }

    @Override
    public Optional<String> size() { return Optional.of(size); }
}
