package servicecourse.services.bikes;

import lombok.Builder;
import servicecourse.repo.GroupsetEntity;

import java.net.URL;
import java.util.Optional;

@Builder
public class UpdateBikeParams {
    /** Set to null if no update */
    private GroupsetEntity groupset;
    /** Set to null if no update */
    private URL heroImageUrl;

    /**
     * @return the new groupset if there is an update, empty otherwise
     */
    public Optional<GroupsetEntity> groupset() {
        return Optional.ofNullable(groupset);
    }

    /**
     * @return the new hero image URL if there is an update, empty otherwise
     */
    public Optional<URL> heroImageUrl() {
        return Optional.ofNullable(heroImageUrl);
    }
}
