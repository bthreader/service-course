package servicecourse.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import servicecourse.generated.types.BikeBrand;

@Entity
@Table(name = "bike_brands")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BikeBrandEntity {
    @Id
    private String name;

    public static BikeBrandEntity ofName(String name) {
        return new BikeBrandEntity(name);
    }

    public BikeBrand asBikeBrand() {
        return BikeBrand.newBuilder()
                .name(name)
                .build();
    }
}
