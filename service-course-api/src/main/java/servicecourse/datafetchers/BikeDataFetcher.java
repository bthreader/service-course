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
public class BikeDataFetcher {
    private final BikesService bikesService;

    @DgsQuery
    public List<Bike> bikes(@InputArgument BikesFilterInput filter) {
        return bikesService.bikes(filter);
    }

    @DgsMutation
    public Bike createBike(@InputArgument CreateBikeInput input) {
        return bikesService.createBike(input);
    }

    @DgsMutation
    public Bike updateBike(@InputArgument UpdateBikeInput input) {
        return bikesService.updateBike(input);
    }

    @DgsMutation
    public String deleteBike(@InputArgument String id) {
        return bikesService.deleteBike(id).toString();
    }
}
