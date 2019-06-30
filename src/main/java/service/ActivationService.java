package service;

import java.io.Serializable;

public class ActivationService extends TokenService implements Serializable {

    private static ActivationService activationService;

    private ActivationService() {
        super();
    }

    public static ActivationService getInstance() {
        if (activationService == null) {
            activationService = new ActivationService();
        }
        return activationService;
    }

    public void sendActivationMail(String email) {

        String token = super.generate(email);

        MailService mailService = MailService.getInstance();

        try {

            mailService.sendActivationToken(email, token);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
