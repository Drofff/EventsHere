package bean;

import java.io.Serializable;
import java.util.*;

public class ForgotService implements Serializable {

    private static ForgotService forgotService;

    private Map<String, String> accessTokens = new HashMap<>();

    private ForgotService() {}

    public static ForgotService getInstance() {
        if (forgotService == null) {
            forgotService = new ForgotService();
        }
        return forgotService;
    }

    public String generateToken(String username) {

        String token = UUID.randomUUID().toString();

        accessTokens.put(username, token);

        return token;

    }

    public String checkToken(String token) {

        if (accessTokens.containsValue(token)) {

            String email = accessTokens.entrySet().stream().filter(x -> x.getValue().equals(token)).findFirst().get().getKey();

            invalidateToken(email);

            return email;

        }

        return "";

    }

    public void invalidateToken(String username) {
        accessTokens.remove(username);
    }


}