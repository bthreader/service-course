package servicecourse.repo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import servicecourse.generated.types.Bike;
import servicecourse.services.bikes.BikeId;
import servicecourse.services.bikes.CrudBikeInput;

@Entity
@Table(name = "bikes")
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BikeEntity {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private ModelEntity model;

    @ManyToOne
    @JoinColumn(name = "groupset_name")
    private GroupsetEntity groupset;

//    // TODO another converter??
//    @Convert(converter = URI.class)
//    private URL heroImageUrl;

    private String size;

    public Bike asBike() {
        return Bike.newBuilder()
                .id(BikeId.serialize(id))
                .model(model.asModel())
                .groupset(groupset.asGroupset())
                .heroImageUrl(null) // TODO fix the URI stuff
                .size(size)
                .build();
    }

    public void apply(CrudBikeInput crudBikeInput) {
        crudBikeInput.model().ifPresent(this::setModel);
        crudBikeInput.groupset().ifPresent(this::setGroupset);
//        crudBikeInput.heroImageUrl().ifPresent(this::setHeroImageUrl);
    }
}
