package app.repository;

import app.model.Broker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrokerRepository extends CrudRepository<Broker, Integer> {
    public Broker findFirstByName(String name);
    public List<Broker> findAll();
}
