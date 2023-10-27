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

    @ManyToOne
    @JoinColumn(name = "model_id")
    private ModelEntity model;

    @ManyToOne
    @JoinColumn(name = "groupset_name")
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

    public void apply(CrudBikeInput crudBikeInput) {
        crudBikeInput.model().ifPresent(this::setModel);
        crudBikeInput.groupset().ifPresent(this::setGroupset);
        crudBikeInput.heroImageUrl().ifPresent(this::setHeroImageUrl);
        crudBikeInput.size().ifPresent(this::setSize);
    }
}
