package app.service;

import app.model.Broker;
import app.repository.BrokerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class BrokerService {
    @Autowired
    private BrokerRepository brokerRepository;

    public BrokerRepository getBrokerRepository() {
        return brokerRepository;
    }

    public void setBrokerRepository(BrokerRepository brokerRepository) {
        this.brokerRepository = brokerRepository;
    }
}
