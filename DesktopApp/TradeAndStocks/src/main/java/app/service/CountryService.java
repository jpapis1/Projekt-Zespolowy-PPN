package app.service;

import app.model.Country;
import app.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;

    public CountryRepository getRepo() {
        return countryRepository;
    }
}
