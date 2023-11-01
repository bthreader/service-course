package servicecourse.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bike_brands")
public class BikeBrandEntity {
    @Id
    private String name;
}
