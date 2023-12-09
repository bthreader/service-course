package servicecourse.services.groupsets;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import servicecourse.generated.types.CreateGroupsetInput;
import servicecourse.generated.types.Groupset;
import servicecourse.repo.GroupsetEntity;
import servicecourse.repo.GroupsetRespository;
import servicecourse.services.exceptions.GroupsetAlreadyExistsException;
import servicecourse.services.exceptions.GroupsetNotFoundException;

@Service
@RequiredArgsConstructor
public class GroupsetServiceImpl implements GroupsetService {
    private final GroupsetRespository groupsetRespository;

    @Override
    public Groupset createGroupset(CreateGroupsetInput input) {
        // Validate that the groupset doesn't already exist
        groupsetRespository.findById(input.getName())
                .ifPresent((entity) -> {
                    throw new GroupsetAlreadyExistsException(input.getName());
                });

        GroupsetEntity newGroupset = GroupsetEntity.builder()
                .name(input.getName())
                .brand(input.getBrand())
                .isElectronic(input.getIsElectronic())
                .build();

        return groupsetRespository.save(newGroupset).asGroupset();
    }

    @Override
    public String deleteGroupset(String name) {
        return groupsetRespository.findById(name).map((entity) -> {
            groupsetRespository.deleteById(name);
            return name;
        }).orElseThrow(() -> new GroupsetNotFoundException(name));
    }
}
