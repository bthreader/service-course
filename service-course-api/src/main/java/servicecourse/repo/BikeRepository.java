package servicecourse.repo;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BikeRepository extends JpaRepository<BikeEntity, Long>, JpaSpecificationExecutor<BikeEntity> {
    @NotNull
    @Query("SELECT b FROM BikeEntity b JOIN FETCH b.model JOIN FETCH b.groupset")
    List<BikeEntity> findAll();
}
