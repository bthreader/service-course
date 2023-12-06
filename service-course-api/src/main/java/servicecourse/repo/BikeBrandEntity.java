package servicecourse.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import servicecourse.generated.types.BikeBrand;

@Entity
@Table(name = "bike_brands")
@EqualsAndHashCode
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
public class BikeBrandEntity {
    @Id
    @NonNull
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
