package servicecourse.services.bikes;

import lombok.Builder;
import servicecourse.repo.GroupsetEntity;

import java.net.URL;
import java.util.Optional;

@Builder
public class UpdateBikeParams {
    private Optional<GroupsetEntity> groupset;
    private URL heroImageUrl;

    public Optional<GroupsetEntity> groupset() {
        return groupset;
    }

    public Optional<URL> heroImageUrl() {
        return Optional.ofNullable(heroImageUrl);
    }
}
