package servicecourse.services.bikebrands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import servicecourse.generated.types.BikeBrand;
import servicecourse.generated.types.CreateBikeBrandInput;
import servicecourse.repo.BikeBrandEntity;
import servicecourse.repo.BikeBrandRepository;
import servicecourse.services.Errors;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelationalBikeBrandsService implements BikeBrandsService {
    private final BikeBrandRepository bikeBrandRepository;

    @Override
    public BikeBrand createBikeBrand(CreateBikeBrandInput input) {
        // Validate that the brand doesn't already exist
        bikeBrandRepository.findById(input.getName())
                .ifPresent((entity) -> { throw Errors.newBikeBrandAlreadyExistsError(); });

        return bikeBrandRepository.save(BikeBrandEntity.ofName(input.getName())).asBikeBrand();
    }

    @Override
    public String deleteBikeBrand(String name) {
        return bikeBrandRepository.findById(name).map((entity) -> {
            bikeBrandRepository.deleteById(name);
            return name;
        }).orElseThrow(Errors::newBikeBrandNotFoundError);
    }

    @Override
    public List<BikeBrand> bikeBrands() {
        return bikeBrandRepository.findAll()
                .stream()
                .map(BikeBrandEntity::asBikeBrand)
                .collect(Collectors.toList());
    }
}
