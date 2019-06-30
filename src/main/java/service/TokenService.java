package service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenService {

    private static TokenService tokenService;

    private Map<String, String> accessTokens = new HashMap<>();

    protected TokenService() {}

    public static TokenService getInstance() {
        if (tokenService == null) {
            tokenService = new TokenService();
        }
        return tokenService;
    }

    public String generate(String username) {

        String token = UUID.randomUUID().toString();

        accessTokens.put(username, token);

        return token;

    }

    public String use(String token) {

        if (accessTokens.containsValue(token)) {

            String email = accessTokens.entrySet().stream().filter(x -> x.getValue().equals(token)).findFirst().get().getKey();

            invalidate(email);

            return email;

        }

        return "";

    }

    public void invalidate(String username) {
        accessTokens.remove(username);
    }

}
