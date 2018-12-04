package app.service;

import app.model.User;
import app.repository.UserRepository;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.jws.soap.SOAPBinding;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class UserService {
    private static UserRepository userRepository;
    private static User activeUser;
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

    public static boolean isPasswordCorrect(String usernameOrEmail, String password) {
        User user = userRepository.findFirstByUsername(usernameOrEmail);
        if(user == null ) { // username not found
            user = userRepository.findFirstByEmail(usernameOrEmail);
        }
        if(user == null) return false; // neither username nor email was found
        String[] pass = user.getPassword().split("\\$");
        KeySpec spec = new PBEKeySpec(password.toCharArray(),pass[2].getBytes(),Integer.valueOf(pass[1]),256);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            String passHash = Base64.encode(f.generateSecret(spec).getEncoded());
            if(pass[3].equals(passHash)) {
                setActiveUser(user);
                return true;
            } else {
                return false;
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("ERROR");
        }
        return false;
    }
    public static void setActiveUser(User user) {
        activeUser = user;
    }
}
