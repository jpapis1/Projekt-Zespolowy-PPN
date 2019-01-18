package app.service;

import app.model.Broker;
import app.repository.BrokerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrokerService {
    @Autowired
    private BrokerRepository brokerRepository;

    public Broker getBrokerByName(String name) {
        return brokerRepository.findFirstByName(name);
    }
    public List<Broker> getAllBrokers() {
        return brokerRepository.findAll();
    }

}
