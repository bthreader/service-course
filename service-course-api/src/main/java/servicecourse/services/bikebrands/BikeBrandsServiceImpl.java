package servicecourse.services.bikebrands;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import servicecourse.configuration.CacheConfiguration;
import servicecourse.generated.types.BikeBrand;
import servicecourse.generated.types.CreateBikeBrandInput;
import servicecourse.repo.BikeBrandEntity;
import servicecourse.repo.BikeBrandRepository;
import servicecourse.services.common.exceptions.BikeBrandAlreadyExistsException;
import servicecourse.services.common.exceptions.BikeBrandNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BikeBrandsServiceImpl implements BikeBrandsService {
    private final BikeBrandRepository bikeBrandRepository;

    @Override
    @CacheEvict(value = CacheConfiguration.BIKE_BRANDS)
    public BikeBrand createBikeBrand(CreateBikeBrandInput input) {
        // Validate that the brand doesn't already exist
        bikeBrandRepository.findById(input.getName())
                .ifPresent((entity) -> {
                    throw new BikeBrandAlreadyExistsException(input.getName());
                });

        return bikeBrandRepository.save(BikeBrandEntity.ofName(input.getName())).asBikeBrand();
    }

    @Override
    @CacheEvict(value = CacheConfiguration.BIKE_BRANDS)
    public String deleteBikeBrand(String name) {
        return bikeBrandRepository.findById(name).map((entity) -> {
            bikeBrandRepository.deleteById(name);
            return name;
        }).orElseThrow(() -> new BikeBrandNotFoundException(name));
    }

    /**
     * This method is cached. The cache is invalidated by {@link #createBikeBrand} and
     * {@link #deleteBikeBrand}.
     */
    @Override
    @Cacheable(value = CacheConfiguration.BIKE_BRANDS, sync = true)
    public List<BikeBrand> bikeBrands() {
        return bikeBrandRepository.findAll()
                .stream()
                .map(BikeBrandEntity::asBikeBrand)
                .toList();
    }
}
