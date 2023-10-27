package servicecourse.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import servicecourse.generated.types.Bike;
import servicecourse.generated.types.BikesFilterInput;
import servicecourse.generated.types.CreateBikeInput;
import servicecourse.generated.types.UpdateBikeInput;
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
    public Bike createBike(@InputArgument CreateBikeInput createBikeInput) {
        return null;
    }

    @DgsMutation
    public Bike updateBike(@InputArgument UpdateBikeInput updateBikeInput) {
        return bikesService.updateBike(updateBikeInput);
    }
}
