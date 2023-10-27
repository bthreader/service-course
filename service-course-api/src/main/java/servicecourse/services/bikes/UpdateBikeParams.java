package servicecourse.services.bikes;

import lombok.Builder;
import servicecourse.repo.GroupsetEntity;
import servicecourse.repo.ModelEntity;

import java.net.URL;
import java.util.Optional;

@Builder
public class UpdateBikeParams implements CrudBikeInput {
    private Optional<GroupsetEntity> groupset;
    private URL heroImageUrl;

    @Override
    public Optional<GroupsetEntity> groupset() {
        return groupset;
    }

    @Override
    public Optional<URL> heroImageUrl() {
        return Optional.ofNullable(heroImageUrl);
    }

    // Model and size cannot be updated, so always return empty

    @Override
    public Optional<ModelEntity> model() {
        return Optional.empty();
    }

    @Override
    public Optional<String> size() { return Optional.empty(); }
}
