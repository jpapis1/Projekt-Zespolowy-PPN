package app.service;

import app.model.Country;
import app.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;
    public void addCountry(String name, double taxRate) {
        countryRepository.save(new Country(name,taxRate));
    }
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public void removeCountry(Country selectedItem) {
        countryRepository.delete(selectedItem);
    }
}
