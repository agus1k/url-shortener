package org.agus.springboot.redirectproject.controllers;

import jakarta.validation.Valid;
import org.agus.springboot.redirectproject.dtos.UrlDTO;
import org.agus.springboot.redirectproject.entities.Url;
import org.agus.springboot.redirectproject.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/shorten/{shortCode}")
    public ResponseEntity<?> getShortUrl(@PathVariable String shortCode) {
        Optional<Url> shortUrl = urlService.findByShortCode(shortCode);
        if (shortUrl.isPresent()) {
            urlService.incrementAccessCount(shortCode);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(shortUrl.get().getUrl()));

            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).headers(headers).build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/shorten/{shortCode}/stats")
    public ResponseEntity<?> getShortUrlStats(@PathVariable String shortCode) {
        return urlService.findByShortCode(shortCode)
                .map(url -> ResponseEntity.ok(url)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> createShortUrl(@Valid @RequestBody Url url, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }

        if (!urlService.urlValid(url.getUrl())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Por favor, ingresa una url valida.");
        }

        url.setShortCode(urlService.generateShortCode());
        urlService.save(url);

        return ResponseEntity.status(HttpStatus.CREATED).body(urlService.convertToDTO(url));
    }

    @PutMapping("/shorten/{shortCode}")
    public ResponseEntity<?> updateShortUrl(@Valid @RequestBody Url url, BindingResult bindingResult, @PathVariable String shortCode){
        if (bindingResult.hasErrors()) {
            return validation(bindingResult);
        }
        if (!urlService.urlValid(url.getUrl())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Por favor, ingresa una url valida.");
        }

        return urlService.findByShortCode(shortCode)
                .map(urlDb -> {
                    urlDb.setUrl(url.getUrl());
                    urlService.save(urlDb);

                    return ResponseEntity.ok(urlService.convertToDTO(urlDb));
                }).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/shorten/{shortCode}")
    public ResponseEntity<?> deleteShortUrl(@PathVariable String shortCode) {
        return urlService.findByShortCode(shortCode)
                .map(url -> {urlService.delete(url);
                        return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // ******* Validation ********

    private ResponseEntity<?> validation(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();


        bindingResult.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo "+error.getField()+" "+error.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
