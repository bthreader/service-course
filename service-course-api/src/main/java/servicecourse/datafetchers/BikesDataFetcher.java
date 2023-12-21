package servicecourse.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import servicecourse.generated.types.*;
import servicecourse.services.bikes.BikesService;

@DgsComponent
@RequiredArgsConstructor
public class BikesDataFetcher {
    private final BikesService bikesService;

    @DgsQuery
    public BikeConnection bikes(@InputArgument BikesFilterInput filter, @InputArgument int first,
                                @InputArgument CursorInput after) {
        return bikesService.bikes(filter, first, after);
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
