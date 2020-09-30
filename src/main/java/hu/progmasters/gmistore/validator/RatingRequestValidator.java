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

        if (ratingRequest.getTitle() != null && ratingRequest.getTitle().length() > 100) {
            errors.rejectValue("title", "rating.title.tooLong");
        }

        if (ratingRequest.getPositiveComment() == null) {
            errors.rejectValue("positiveComment", "rating.emptyRating");
        } else if (ratingRequest.getPositiveComment().length() < 3) {
            errors.rejectValue("positiveComment", "rating.tooShort");
        } else if (ratingRequest.getPositiveComment().length() > 1000) {
            errors.rejectValue("positiveComment", "rating.tooLong");
        }

        if (ratingRequest.getNegativeComment() == null) {
            errors.rejectValue("positiveComment", "rating.emptyRating");
        } else if (ratingRequest.getNegativeComment().length() < 3) {
            errors.rejectValue("positiveComment", "rating.tooShort");
        } else if (ratingRequest.getNegativeComment().length() > 1000) {
            errors.rejectValue("positiveComment", "rating.tooLong");
        }
    }
}
