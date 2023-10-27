package servicecourse.repo;

import jakarta.persistence.*;
import lombok.*;
import servicecourse.generated.types.Groupset;
import servicecourse.generated.types.GroupsetBrand;

@Entity
@Table(name = "groupsets")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GroupsetEntity {
    @Id
    private String name;
    @Enumerated(EnumType.STRING)
    private GroupsetBrand brand;
    private boolean isElectronic;

    public Groupset asGroupset() {
        return Groupset.newBuilder()
                .name(name)
                .brand(brand)
                .isElectronic(isElectronic)
                .build();
    }
}
