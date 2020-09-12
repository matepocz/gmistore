package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.MainCategoryDetails;
import hu.progmasters.gmistore.dto.NewCategoryRequest;
import hu.progmasters.gmistore.dto.PaymentMethodDetails;
import hu.progmasters.gmistore.dto.ProductCategoryDetails;
import hu.progmasters.gmistore.service.LookupService;
import hu.progmasters.gmistore.validator.NewCategoryRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/lookup")
public class LookupController {

    private final LookupService lookupService;
    private final NewCategoryRequestValidator newCategoryRequestValidator;

    @Autowired
    public LookupController(LookupService lookupService, NewCategoryRequestValidator newCategoryRequestValidator) {
        this.lookupService = lookupService;
        this.newCategoryRequestValidator = newCategoryRequestValidator;
    }

    @InitBinder
    public void binder(WebDataBinder binder) {
        binder.addValidators(newCategoryRequestValidator);
    }

    @GetMapping("/payment-methods")
    public ResponseEntity<List<PaymentMethodDetails>> getPaymentMethods() {
        List<PaymentMethodDetails> paymentMethods = lookupService.getPaymentMethods();
        return new ResponseEntity<>(paymentMethods, HttpStatus.OK);
    }

    @GetMapping("/main-product-categories")
    public ResponseEntity<List<ProductCategoryDetails>> getMainProductCategories() {
        List<ProductCategoryDetails> categories = lookupService.getMainProductCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/sub-product-categories/{id}")
    public ResponseEntity<List<ProductCategoryDetails>> getSubCategories(@PathVariable("id") Long id) {
        List<ProductCategoryDetails> categories = lookupService.getSubProductCategories(id);
        return categories != null ?
                new ResponseEntity<>(categories, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<MainCategoryDetails>> getCategories() {
        List<MainCategoryDetails> categories = lookupService.getCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/new-category")
    public ResponseEntity<Boolean> createCategory(@Valid @RequestBody NewCategoryRequest newCategoryRequest) {
        boolean result = lookupService.createCategory(newCategoryRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
