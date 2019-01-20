package app.repository;

import app.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {
    public Country findFirstByName(String name);
    public List<Country> findAll();
}
