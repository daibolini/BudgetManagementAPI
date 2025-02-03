package ca.vanier.budgetmanagementapi.controller;

import java.text.NumberFormat;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class GreetingController {

    @Autowired
    private MessageSource messageSource;

    

    @GetMapping("/greeting")
    public ResponseEntity<String> greet(@RequestHeader(name="Accept-Language", required = false) String acceptLanguage) {
        Locale locale = acceptLanguage != null ? Locale.forLanguageTag(acceptLanguage) : Locale.getDefault();
        System.out.println(locale.toString());
        
        // Retrieve the localized message
        String greetingMessage = messageSource.getMessage("greeting", null, locale);
        return ResponseEntity.ok(greetingMessage);
    }

    @GetMapping("/currency")
    public ResponseEntity<String> calculate(@RequestHeader(name = "Accept-Language", required = false) String acceptLanguage) {
        // Determine the locale from the Accept-Language header or default locale
        Locale locale = acceptLanguage != null ? Locale.forLanguageTag(acceptLanguage) : Locale.getDefault();
        System.out.println("Locale: " + locale.toString());

        // Format a sample number as currency in the specified locale
        double amount = 12345.67;  // Example amount
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        String formattedCurrency = currencyFormatter.format(amount);

        // Return formatted currency value
        return ResponseEntity.ok(formattedCurrency);
    }

}