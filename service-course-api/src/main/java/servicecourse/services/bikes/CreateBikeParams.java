package servicecourse.services.bikes;

import lombok.Builder;
import lombok.NonNull;
import servicecourse.repo.GroupsetEntity;
import servicecourse.repo.ModelEntity;

import java.net.URL;
import java.util.Optional;

@Builder
public class CreateBikeParams implements CrudBikeInput {
    @NonNull
    private ModelEntity modelEntity;
    @NonNull
    private GroupsetEntity groupsetEntity;
    @NonNull
    private String size;
    private URL heroImageUrl;

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
        return Optional.ofNullable(heroImageUrl);
    }

    @Override
    public Optional<String> size() { return Optional.of(size); }
}
