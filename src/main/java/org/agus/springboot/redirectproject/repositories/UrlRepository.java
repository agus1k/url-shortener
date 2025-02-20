package org.agus.springboot.redirectproject.repositories;

import org.agus.springboot.redirectproject.entities.Url;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends CrudRepository<Url, Long> {
    List<Url> url(String url);

    Boolean existsByShortCode(String shortCode);

    Optional<Url> findByShortCode(String shortCode);
}
