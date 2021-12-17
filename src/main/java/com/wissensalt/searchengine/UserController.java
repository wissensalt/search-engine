package com.wissensalt.searchengine;

import com.wissensalt.searchengine.dto.UserResponse;
import com.wissensalt.searchengine.es.UserES;
import com.wissensalt.searchengine.es.UserESRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Slf4j
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
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String [] fullName;
        if (StringUtils.isNotBlank(name) && name.contains(" ")) {
            fullName = name.split(" ");
        } else {
            fullName = new String [0];
        }
        final String noSpaceName = name.replace(" ", "");

        final Page<UserES> userESList = userESRepository
                .findAllByFirstNameContainingOrMiddleNameContainingOrLastNameContaining(
                        fullName.length > 0 && StringUtils.isNotBlank(fullName[0]) ? fullName[0] : noSpaceName,
                        fullName.length > 1 && StringUtils.isNotBlank(fullName[1]) ? fullName[1] : noSpaceName,
                        fullName.length > 2 && StringUtils.isNotBlank(fullName[2]) ? fullName[2] : noSpaceName,
                        PageRequest.of(page, limit)
                );

        final List<UserResponse> userResponses = emptyIfNull(userESList.get().collect(toList()))
                .stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .middleName(user.getMiddleName())
                        .lastName(user.getLastName())
                        .build())
                .collect(toList());

        stopWatch.stop();
        log.info("Time taken for searching {} users is {} ms", limit, stopWatch.getTotalTimeMillis());

        return userResponses;
    }
}
