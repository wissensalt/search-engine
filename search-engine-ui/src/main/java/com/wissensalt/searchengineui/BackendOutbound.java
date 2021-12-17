package com.wissensalt.searchengineui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BackendOutbound {
    private BackendOutbound(){}

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendOutbound.class);

    static List<UserDTO> getUsers(String query, String limit, String page) {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);

        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<UserDTO>> entity = new HttpEntity<>(headers);

        final Map<String, String> params = new HashMap<>();
        params.put("name",  URLEncoder.encode(query, StandardCharsets.UTF_8));
        params.put("limit", limit);
        params.put("page", page);

        ResponseEntity<List<UserDTO>> response;

        try{
            response = restTemplate.exchange(
                    "http://api:8080/users?name={name}&limit={limit}&page={page}",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
                    , params
            );
        } catch (Exception e) {
            LOGGER.warn(String.format("Failed to get initial user list : %s", e.getMessage()));
            response = new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }

        if(response.getStatusCode() != HttpStatus.OK) {
            return Collections.emptyList();
        }

        return response.getBody();
    }
}
