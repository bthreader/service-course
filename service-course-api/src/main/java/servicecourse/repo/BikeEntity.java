package servicecourse.repo;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import servicecourse.generated.types.Bike;
import servicecourse.repo.common.URLConverter;
import servicecourse.services.bikes.BikeId;
import servicecourse.services.bikes.UpdateBikeParams;

import java.net.URL;

@Entity
@Table(name = "bikes")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private ModelEntity model;

    @ManyToOne
    @JoinColumn(name = "groupset_name")
    private GroupsetEntity groupset;

    @Nullable
    @Convert(converter = URLConverter.class)
    private URL heroImageUrl;

    private String size;

    public Bike asBike() {
        return Bike.newBuilder()
                .id(BikeId.serialize(id))
                .model(model.asModel())
                .groupset(groupset.asGroupset())
                .heroImageUrl(heroImageUrl)
                .size(size)
                .build();
    }

    /**
     * Apply the updates ready for persisting
     *
     * @param input the details of an update to a bike entity
     */
    public void apply(UpdateBikeParams input) {
        input.groupset().ifPresent(this::setGroupset);
        input.heroImageUrl().ifPresent(this::setHeroImageUrl);
    }

    private void setGroupset(GroupsetEntity groupset) {
        this.groupset = groupset;
    }

    private void setHeroImageUrl(URL heroImageUrl) {
        this.heroImageUrl = heroImageUrl;
    }
}
