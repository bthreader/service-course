package servicecourse.repo;

import jakarta.persistence.*;
import lombok.*;
import servicecourse.generated.types.Bike;
import servicecourse.services.bikes.BikeId;
import servicecourse.services.bikes.CrudBikeInput;

import java.net.URL;

@Entity
@Table(name = "bikes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * TODO this is not causing joins
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "model_id", nullable = false)
    private ModelEntity model;

    /**
     * TODO this is not causing joins
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "groupset_name", nullable = false)
    private GroupsetEntity groupset;

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

    public void apply(CrudBikeInput input) {
        input.model().ifPresent(this::setModel);
        input.groupset().ifPresent(this::setGroupset);
        input.heroImageUrl().ifPresent(this::setHeroImageUrl);
        input.size().ifPresent(this::setSize);
    }
}
