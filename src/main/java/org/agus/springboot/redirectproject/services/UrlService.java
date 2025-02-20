package org.agus.springboot.redirectproject.services;

import org.agus.springboot.redirectproject.dtos.UrlDTO;
import org.agus.springboot.redirectproject.entities.Url;
import org.agus.springboot.redirectproject.repositories.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    @Autowired
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public void save(Url url) {
        urlRepository.save(url);
    }

    public void delete(Url url) {
        urlRepository.delete(url);
    }

    public Optional<Url> findByShortCode(String shortCode) {
        return urlRepository.findByShortCode(shortCode);
    }

    public UrlDTO convertToDTO(Url url) {
        return new UrlDTO(url.getId(), url.getUrl(), url.getShortCode(), url.getCreatedAt(), url.getUpdatedAt());
    }

    public String generateShortCode() {
        String characters = "abcdefghijkmlnopqrstuvwxyz1234567890";
        SecureRandom random = new SecureRandom();

        String code;
        do {
            StringBuilder sb = new StringBuilder(6);
            for (int i = 0; i < 6; i++) {
                sb.append(characters.charAt(random.nextInt(characters.length())));
            }
            code = sb.toString();
        } while (urlRepository.existsByShortCode(code));
        return code;
    }

    public boolean urlValid(String url) {
        try{
            URI uri = new URI(url);
            return uri.isAbsolute();
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public void incrementAccessCount(String shortCode) {
        Optional<Url> urlOptional = urlRepository.findByShortCode(shortCode);
        if (urlOptional.isPresent()) {
            Url url = urlOptional.get();

            if(url.getAccessCount() == null) {
                url.setAccessCount(0L);
            }

            long newCount = url.getAccessCount() + 1L;

            url.setAccessCount(url.getAccessCount() + 1);
            urlRepository.save(url);
        } else {
            throw new RuntimeException("URL not found");
        }
    }
}