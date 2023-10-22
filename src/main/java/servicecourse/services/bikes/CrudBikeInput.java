package servicecourse.services.bikes;

import servicecourse.repo.GroupsetEntity;
import servicecourse.repo.ModelEntity;

import java.net.URL;
import java.util.Optional;

public interface CrudBikeInput {
    Optional<ModelEntity> model();

    Optional<GroupsetEntity> groupset();

    Optional<URL> heroImageUrl();
}
