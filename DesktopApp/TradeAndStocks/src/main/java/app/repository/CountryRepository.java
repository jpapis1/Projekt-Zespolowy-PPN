package app.repository;

import app.model.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Integer> {
    public Country findFirstByName(String name);
}
