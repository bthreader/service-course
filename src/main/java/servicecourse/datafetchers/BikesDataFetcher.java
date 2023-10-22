package servicecourse.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import servicecourse.generated.types.*;
import servicecourse.services.bikes.BikesService;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class BikesDataFetcher {
    private final BikesService bikesService;

    @DgsQuery
    public List<Bike> bikes(@InputArgument BikesFilterInput bikesFilterInput) {
        return bikesService.bikes(bikesFilterInput);
    }

    @DgsMutation
    public CreateBikeResponse createBike(@InputArgument CreateBikeInput createBikeInput) {
        return null;
    }

    @DgsMutation
    public UpdateBikeResponse updateBike(@InputArgument UpdateBikeInput updateBikeInput) {
        return bikesService.updateBike(updateBikeInput)
                .map(bike -> UpdateBikeResponse.newBuilder()
                        .bike(bike)
                        .success(true)
                        .build())
                .orElse(UpdateBikeResponse.newBuilder().success(false).build());
    }
}
