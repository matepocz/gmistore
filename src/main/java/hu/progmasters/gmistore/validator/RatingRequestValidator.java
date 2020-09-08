package hu.progmasters.gmistore.validator;

import hu.progmasters.gmistore.dto.NewRatingRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RatingRequestValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return NewRatingRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewRatingRequest ratingRequest = (NewRatingRequest) target;
        if (ratingRequest.getActualRating() == null) {
            errors.rejectValue("actualRating", "rating.emptyRating");
        }
    }
}
