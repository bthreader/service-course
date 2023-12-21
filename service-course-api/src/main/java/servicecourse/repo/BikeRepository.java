package servicecourse.repo;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BikeRepository extends JpaRepository<BikeEntity, Long>, JpaSpecificationExecutor<BikeEntity> {
    @NonNull
    @Query("SELECT b FROM BikeEntity b JOIN FETCH b.model JOIN FETCH b.groupset")
    Page<BikeEntity> findAll(@NotNull Specification<BikeEntity> spec, @NotNull Pageable pageable);
}
