package app.service;

import app.model.Country;
import app.repository.CountryRepository;

public class CountryService {
    private static CountryRepository countryRepository;
    private static boolean initialized = false;
    public static void initialize(CountryRepository repo) {
        if(!initialized) {
            countryRepository = repo;
            countryRepository.save(new Country("Poland",0.19));
            countryRepository.save(new Country("United States",0.15));
            countryRepository.save(new Country("Germany",0.25));
            initialized = true;
        }
    }

    public static CountryRepository getRepo() {
        return countryRepository;
    }
}
