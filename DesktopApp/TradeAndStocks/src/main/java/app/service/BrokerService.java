package app.service;

import app.model.Broker;
import app.repository.BrokerRepository;

public class BrokerService {
    private static BrokerRepository brokerRepository;
    private static boolean initialized = false;
    public static void initialize(BrokerRepository repo) {
        if(!initialized) {
            brokerRepository = repo;
            brokerRepository.save(new Broker("Bank Of America", 0.001, 0.005, CountryService.getRepo().findFirstByName("United States")));
            brokerRepository.save(new Broker("JP Morgan", 0.0, 0.001, CountryService.getRepo().findFirstByName("United States")));
            brokerRepository.save(new Broker("PKO", 0.002, 0.001, CountryService.getRepo().findFirstByName("Poland")));
            initialized = true;
        }
    }

    public static BrokerRepository getRepo() {
        return brokerRepository;
    }
}
