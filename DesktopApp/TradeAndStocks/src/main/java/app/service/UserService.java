package app.service;

import app.repository.UserRepository;

public class UserService {
    private static UserRepository userRepository;
    private static boolean initialized = false;
    public static void initialize(UserRepository repo) {
        if(!initialized) {
            userRepository = repo;
            initialized = true;
        }
    }

    public static UserRepository getRepo() {
        return userRepository;
    }
}
