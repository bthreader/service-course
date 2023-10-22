package servicecourse.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import servicecourse.generated.types.Model;
import servicecourse.services.models.ModelId;

@Entity
@Table(name = "models")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelEntity {
    @Id
    private Long id;
    private String name;
    private int modelYear;
    private String brandName;

    public Model asModel() {
        return Model.newBuilder()
                .id(ModelId.serialize(id))
                .name(name)
                .modelYear(modelYear)
                .build();
    }
}
