package app.service;

import app.model.Broker;
import app.model.Country;
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
    public void addBroker(String name, double profitMargin, double handlingFee, Country country) {
        brokerRepository.save(new Broker(name,profitMargin,handlingFee,country));
    }


    public void removeBroker(Broker selectedItem) {
        brokerRepository.delete(selectedItem);
    }
}
