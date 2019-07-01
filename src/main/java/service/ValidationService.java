package service;

import dto.EventDto;
import dto.HashTagDto;
import entity.Event;
import entity.Profile;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
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

    public <T> Map<String, String> validate(T t) {

        Map<String, String> errors = new HashMap<>();

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        HashTagDto hashTagDto = HashTagDto.getInstance(session);

        Set<ConstraintViolation<T>> validationResult = validator.validate(t);

        validationResult.stream().forEach(x -> errors.put(x.getPropertyPath().toString() + "Error", x.getMessage()));

        if (t instanceof Event) {
            String tagsValidation = validateTags(hashTagDto.findAll());

            if (tagsValidation != null) {
                errors.put("tagError", tagsValidation);
            }
        }

        return errors;

    }

    public String validateTags(List<String> tagList) {

        HashTagDto hashTagDto = HashTagDto.getInstance(session);

        List<String> tags = hashTagDto.findAll();

        for (String tag : tagList) {

            if (!tags.contains(tag)) {
                return "Invalid tag '" + tag + "'";
            }

        }

        return null;
    }

}
