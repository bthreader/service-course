package servicecourse.repo;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import servicecourse.generated.types.Bike;
import servicecourse.generated.types.BikeConnectionEdge;
import servicecourse.repo.common.URLConverter;
import servicecourse.services.bikes.BikeId;
import servicecourse.services.bikes.UpdateBikeParams;
import servicecourse.utils.Base64ToLongConverter;

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

    public BikeConnectionEdge asBikeConnectionEdge() {
        return BikeConnectionEdge.newBuilder()
                .node(this.asBike())
                .cursor(this.base64Id())
                .build();
    }

    public String base64Id() {
        return Base64ToLongConverter.encodeToBase64(id);
    }

    private void setGroupset(GroupsetEntity groupset) {
        this.groupset = groupset;
    }

    private void setHeroImageUrl(@Nullable URL heroImageUrl) {
        this.heroImageUrl = heroImageUrl;
    }
}
