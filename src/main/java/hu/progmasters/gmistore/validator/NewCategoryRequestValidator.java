package hu.progmasters.gmistore.validator;

import hu.progmasters.gmistore.dto.NewCategoryRequest;
import hu.progmasters.gmistore.enums.DomainType;
import hu.progmasters.gmistore.model.LookupEntity;
import hu.progmasters.gmistore.repository.LookupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class NewCategoryRequestValidator implements Validator {

    private final LookupRepository lookupRepository;

    @Autowired
    public NewCategoryRequestValidator(LookupRepository lookupRepository) {
        this.lookupRepository = lookupRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return NewCategoryRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewCategoryRequest newCategoryRequest = (NewCategoryRequest) target;
        if (newCategoryRequest.getIsSubCategory().equals(true) && newCategoryRequest.getMainCategoryKey() == null) {
            errors.rejectValue("mainCategoryKey", "mainCategoryKey.empty");
        }

        if (newCategoryRequest.getKey() == null) {
            errors.rejectValue("key", "category.empty");
        } else if (newCategoryRequest.getKey().length() < 3) {
            errors.rejectValue("key", "category.short");
        } else if (newCategoryRequest.getKey().length() > 30) {
            errors.rejectValue("key", "category.tooLong");
        } else if (!newCategoryRequest.getKey().matches("^[-'a-zA-ZÀ-ÖØ-öø-ſ _]+$")) {
            errors.rejectValue("key", "category.invalid");
        }

        if (newCategoryRequest.getKey() != null) {
            Optional<LookupEntity> categoryByKey =
                    lookupRepository.findByDomainTypeAndLookupKey(DomainType.PRODUCT_CATEGORY,
                            newCategoryRequest.getKey());
            if (categoryByKey.isPresent()) {
                errors.rejectValue("key", "category.exists");
            }
        }

        if (newCategoryRequest.getDisplayName() == null) {
            errors.rejectValue("displayName", "category.empty");
        } else if (newCategoryRequest.getDisplayName().length() < 3) {
            errors.rejectValue("displayName", "category.short");
        } else if (newCategoryRequest.getDisplayName().length() > 30) {
            errors.rejectValue("displayName","category.tooLong");
        } else if (!newCategoryRequest.getDisplayName().matches("^[-'a-zA-ZÀ-ÖØ-öø-ſ _]+$")) {
            errors.rejectValue("displayName", "category.invalid");
        }
    }
}
