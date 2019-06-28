package bean;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

public class ValidationService implements Serializable {

    private static ValidationService validationService;

    private static HttpSession session;

    private ValidationService() {}

    public static ValidationService getInstance(HttpSession httpSession) {
        if (validationService == null) {
            validationService = new ValidationService();
            session = httpSession;
        }
        return validationService;
    }

    public boolean validateEmail(String email) {
        return email.matches(".*@.*\\..*");

    }

}
