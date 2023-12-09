package servicecourse.services.groupsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import servicecourse.generated.types.CreateGroupsetInput;
import servicecourse.generated.types.Groupset;
import servicecourse.generated.types.GroupsetBrand;
import servicecourse.repo.GroupsetEntity;
import servicecourse.repo.GroupsetRespository;
import servicecourse.services.EntityFactory;
import servicecourse.services.exceptions.GroupsetAlreadyExistsException;
import servicecourse.services.exceptions.GroupsetNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupsetServiceImplTest {
    @Mock
    GroupsetRespository mockGroupsetRepository;
    GroupsetServiceImpl groupsetService;

    @BeforeEach
    void setup() {
        groupsetService = new GroupsetServiceImpl(mockGroupsetRepository);
    }

    @Nested
    class createBikeBrand {
        @Test
        void fail_because_groupset_already_exists() {
            // Given a groupset which already exists
            String groupsetName = "already exists";
            when(mockGroupsetRepository.findById(groupsetName))
                    .thenReturn(Optional.of(EntityFactory.newGroupsetEntityWithName(groupsetName)));

            // When we call the createGroupset method with that name
            // Then the method should throw
            assertThrows(GroupsetAlreadyExistsException.class,
                         () -> groupsetService.createGroupset(CreateGroupsetInput.newBuilder()
                                                                      .name(groupsetName)
                                                                      .brand(GroupsetBrand.SHIMANO)
                                                                      .isElectronic(false)
                                                                      .build()));
        }

        @Test
        void success() {
            // Given a groupset which doesn't exist
            String groupsetName = "105 R7000 11s";
            when(mockGroupsetRepository.findById(groupsetName)).thenReturn(Optional.empty());

            // Given some additional attributes
            GroupsetBrand brand = GroupsetBrand.SHIMANO;
            boolean isElectronic = false;

            // Given an expected entity
            GroupsetEntity expectedEntity = GroupsetEntity.builder()
                    .name(groupsetName)
                    .brand(brand)
                    .isElectronic(isElectronic)
                    .build();

            // When the bike brand repository works as expected
            when(mockGroupsetRepository.save(expectedEntity))
                    .thenReturn(expectedEntity);

            // When we call the createGroupset method with that name
            Groupset result = groupsetService.createGroupset(CreateGroupsetInput.newBuilder()
                                                                     .name(groupsetName)
                                                                     .brand(brand)
                                                                     .isElectronic(isElectronic)
                                                                     .build());

            // Then we should have received the expected Groupset object
            assertThat(result).isEqualTo(expectedEntity.asGroupset());
        }
    }

    @Nested
    class deleteModel {
        @Test
        void fail_because_no_groupset() {
            // Given a groupset which doesn't exist
            String ghostGroupsetName = "Claris 20s";
            when(mockGroupsetRepository.findById(ghostGroupsetName)).thenReturn(Optional.empty());

            // When we call the deleteGroupset method with that name
            // Then the method should throw
            assertThrows(GroupsetNotFoundException.class,
                         () -> groupsetService.deleteGroupset(ghostGroupsetName));
        }

        @Test
        void success() {
            /// Given a groupset which already exists
            String groupsetName = "already exists";
            when(mockGroupsetRepository.findById(groupsetName))
                    .thenReturn(Optional.of(EntityFactory.newGroupsetEntityWithName(groupsetName)));

            // When we call deleteGroupset with that name
            String result = groupsetService.deleteGroupset(groupsetName);

            // Then the repository should have been asked to delete the groupset
            verify(mockGroupsetRepository).deleteById(groupsetName);

            // Then we should receive the name of the deleted groupset back
            assertThat(result).isEqualTo(groupsetName);
        }
    }
}
