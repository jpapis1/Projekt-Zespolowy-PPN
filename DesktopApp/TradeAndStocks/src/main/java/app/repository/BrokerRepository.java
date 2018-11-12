package app.repository;

import app.model.Broker;
import org.springframework.data.repository.CrudRepository;

public interface BrokerRepository  extends CrudRepository<Broker,Integer> {
    public Broker findFirstByName(String name);
}
