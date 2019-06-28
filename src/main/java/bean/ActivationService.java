package bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActivationService implements Serializable {

    private static ActivationService activationService;

    private Map<String, String> tokens = new HashMap<>();

    private ActivationService() {}

    public static ActivationService getInstance() {
        if (activationService == null) {
            activationService = new ActivationService();
        }
        return activationService;
    }

    public void sendActivationMail(String email) {
        String token = UUID.randomUUID().toString();

        tokens.put(email, token);

        MailService mailService = MailService.getInstance();

        try {

            mailService.sendActivationToken(email, token);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String activateByToken(String token) {

        if (tokens.containsValue(token)) {

            String email = tokens.entrySet().stream().filter(x -> x.getValue().equals(token)).findFirst().get().getKey();

            tokens.remove(email);

            return email;
        }

        return "";
    }

}
