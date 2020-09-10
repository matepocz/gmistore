package hu.progmasters.gmistore.controller;

import hu.progmasters.gmistore.dto.PaymentMethodDetails;
import hu.progmasters.gmistore.dto.ProductCategoryDetails;
import hu.progmasters.gmistore.service.LookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lookup")
public class LookupController {

    private final LookupService lookupService;

    @Autowired
    public LookupController(LookupService lookupService) {
        this.lookupService = lookupService;
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
}
