package servicecourse.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupsetRespository extends JpaRepository<GroupsetEntity, String>, JpaSpecificationExecutor<GroupsetEntity> {
}
