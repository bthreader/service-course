package servicecourse.services.bikebrands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import servicecourse.generated.types.BikeBrand;
import servicecourse.generated.types.CreateBikeBrandInput;
import servicecourse.repo.BikeBrandEntity;
import servicecourse.repo.BikeBrandRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RelationalBikeBrandsServiceTest {
    @Mock
    BikeBrandRepository mockBikeBrandRepository;
    RelationalBikeBrandsService relationalBikesBrandsService;

    @BeforeEach
    void setup() {
        relationalBikesBrandsService = new RelationalBikeBrandsService(mockBikeBrandRepository);
    }

    @Nested
    class createBikeBrand {
        @Test
        void fail_because_bike_brand_already_exists() {
            // Given a bike brand which already exists
            String bikeBrandName = "already exists";
            when(mockBikeBrandRepository.findById(bikeBrandName))
                    .thenReturn(Optional.of(BikeBrandEntity.ofName(bikeBrandName)));

            // When we call the createBikeBrand method with that name
            // Then the method should throw
            assertThrows(IllegalArgumentException.class,
                         () -> relationalBikesBrandsService.createBikeBrand(CreateBikeBrandInput.newBuilder()
                                                                                    .name(bikeBrandName)
                                                                                    .build()));
        }

        @Test
        void success() {
            // Given a bike brand which doesn't exist
            String bikeBrandName = "Specialized";
            when(mockBikeBrandRepository.findById(bikeBrandName)).thenReturn(Optional.empty());

            // Given an expected entity
            BikeBrandEntity expectedEntity = BikeBrandEntity.ofName(bikeBrandName);

            // When the bike brand repository works as expected
            when(mockBikeBrandRepository.save(expectedEntity))
                    .thenReturn(expectedEntity);

            // When we call the createBikeBrand method with that name
            BikeBrand result = relationalBikesBrandsService.createBikeBrand(CreateBikeBrandInput.newBuilder()
                                                                                    .name(bikeBrandName)
                                                                                    .build());

            // Then we should have received the expected BikeBrand object
            assertThat(result).isEqualTo(expectedEntity.asBikeBrand());
        }
    }

    @Nested
    class deleteModel {
        @Test
        void fail_because_no_bike_brand() {
            // Given a bike brand which doesn't exist
            String ghostBikeBrandName = "Specialized";
            when(mockBikeBrandRepository.findById(ghostBikeBrandName)).thenReturn(Optional.empty());

            // When we call the deleteBikeBrand method with that name
            // Then the method should throw
            assertThrows(NoSuchElementException.class,
                         () -> relationalBikesBrandsService.deleteBikeBrand(ghostBikeBrandName));
        }

        @Test
        void success() {
            /// Given a bike brand which already exists
            String bikeBrandName = "already exists";
            when(mockBikeBrandRepository.findById(bikeBrandName))
                    .thenReturn(Optional.of(BikeBrandEntity.ofName(bikeBrandName)));

            // When we call deleteBikeBrand with that name
            String result = relationalBikesBrandsService.deleteBikeBrand(bikeBrandName);

            // Then the repository should have been asked to delete the bike brand
            verify(mockBikeBrandRepository).deleteById(bikeBrandName);

            // Then we should receive the name of the deleted bike brand back
            assertThat(result).isEqualTo(bikeBrandName);
        }
    }
}
