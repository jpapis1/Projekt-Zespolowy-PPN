package app.service;

import app.model.User;
import app.repository.UserRepository;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

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

    public static boolean isPasswordCorrect(String username, String password) {
        User user = userRepository.findFirstByUsername(username);
        String[] pass = user.getPassword().split("\\$");
        KeySpec spec = new PBEKeySpec(password.toCharArray(),pass[2].getBytes(),Integer.valueOf(pass[1]),256);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            String passHash = Base64.encode(f.generateSecret(spec).getEncoded());
            return pass[3].equals(passHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("ERROR");
        }
        return false;
    }
}
