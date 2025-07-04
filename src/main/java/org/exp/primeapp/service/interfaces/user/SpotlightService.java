package org.exp.primeapp.service.interfaces.user;

import org.exp.primeapp.models.dto.responce.admin.CategorySpotlightRes;
import org.exp.primeapp.models.dto.responce.user.CategoryRes;
import org.exp.primeapp.models.dto.responce.user.SpotlightRes;
import org.exp.primeapp.models.dto.responce.user.CatalogSpotlightRes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SpotlightService {

    SpotlightRes getSpotlight(Long id);

    List<SpotlightRes> getHeroSpotlights();

    List<CatalogSpotlightRes> getCatalogSpotlightsWithCategories();

    List<CategoryRes> getSpotlightCategories(Long spotlightId);

    List<CategorySpotlightRes> getSpotlightsForCategory();

}
