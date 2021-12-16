package com.wissensalt.searchengine.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserESRepository extends ElasticsearchRepository<UserES, Long> {

    Page<UserES> findAllByFirstNameContainingOrMiddleNameContainingOrLastNameContaining(
            String firstName, String middleName, String lastName, Pageable pageable
    );
}
