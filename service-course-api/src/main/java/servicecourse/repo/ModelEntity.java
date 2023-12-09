package servicecourse.repo;

import jakarta.persistence.*;
import lombok.*;
import servicecourse.generated.types.BikeBrand;
import servicecourse.generated.types.Model;
import servicecourse.services.models.ModelId;

@Entity
@Table(name = "models")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ModelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int modelYear;
    private String brandName;

    public Model asModel() {
        return Model.newBuilder()
                .id(ModelId.serialize(id))
                .name(name)
                .modelYear(modelYear)
                .brand(BikeBrand.newBuilder()
                               .name(brandName)
                               .build())
                .build();
    }
}
