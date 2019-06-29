package bean;

import entity.Event;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public Map<String, String> validateEvent(Event event) {

        Map<String, String> errors = new HashMap<>();

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        EventsService eventsService = EventsService.getInstance(session);

        Set<ConstraintViolation<Event>> validationResult = validator.validate(event);

        validationResult.stream().forEach(x -> errors.put(x.getPropertyPath().toString() + "Error", x.getMessage()));

        String tagsValidation = validateTags(eventsService.findAllTags());

        if (tagsValidation != null) {
            errors.put("tagError", tagsValidation);
        }

        return errors;

    }

    public String validateTags(List<String> tagList) {

        EventsService eventsService = EventsService.getInstance(session);

        List<String> tags = eventsService.findAllTags();

        for (String tag : tagList) {

            if (!tags.contains(tag)) {
                return "Invalid tag '" + tag + "'";
            }

        }

        return null;
    }

}
