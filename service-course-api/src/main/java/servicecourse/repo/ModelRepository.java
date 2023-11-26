package servicecourse.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<ModelEntity, Long>, JpaSpecificationExecutor<ModelEntity> {
    List<ModelEntity> findByBrandName(String brandName);

    List<ModelEntity> findByBrandNameIn(List<String> brandNames);
}
