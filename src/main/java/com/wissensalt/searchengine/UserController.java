package com.wissensalt.searchengine;

import com.wissensalt.searchengine.dto.UserResponse;
import com.wissensalt.searchengine.es.UserES;
import com.wissensalt.searchengine.es.UserESRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserESRepository userESRepository;

    @GetMapping("/users")
    public List<UserResponse> findUsers(
            @RequestParam("name") String name,
            @RequestParam("limit") int limit,
            @RequestParam("page") int page)
    {
        final Page<UserES> userESList = userESRepository
                .findAllByFirstNameContainingOrMiddleNameContainingOrLastNameContaining(
                        name,
                        name,
                        name,
                        PageRequest.of(page, limit)
                );

        return CollectionUtils.emptyIfNull(userESList.get().collect(Collectors.toList()))
                .stream()
                .map(user -> UserResponse.builder()
                        .firstName(user.getFirstName())
                        .middleName(user.getMiddleName())
                        .lastName(user.getLastName())
                        .build())
                .collect(Collectors.toList());
    }
}
