package com.wissensalt.searchengine;

import com.github.javafaker.Faker;
import com.wissensalt.searchengine.mysql.User;
import com.wissensalt.searchengine.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@RequiredArgsConstructor
@Component
public class Seeder {

    @Value("${app.seeding.maxRow}")
    private int maxRow;

    private final UserRepository userRepository;
    private static final int MAX_THREAD_POOL = Runtime.getRuntime().availableProcessors();
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_THREAD_POOL);

    void seed() {
        if (userRepository.countAll() >= maxRow) {
            THREAD_POOL_EXECUTOR.shutdown();
            return;
        }

        if (THREAD_POOL_EXECUTOR.getActiveCount() == 0) {
            for (int a = 1; a<=(MAX_THREAD_POOL); a++) {
                final Task task = new Task("Task "+a);
                THREAD_POOL_EXECUTOR.execute(task);
            }
        }

        seed();
    }

    class Task implements Runnable {

        private String name;

        Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            final long totalRows = userRepository.countAll();
            log.info("{} : Start Row {}", name, totalRows);
            final Faker faker = new Faker(new Locale("en-US"));
            final List<User> users = new ArrayList<>();
            final LocalDateTime now = LocalDateTime.now();
            do {
                final String [] separatedFullName = faker.name().nameWithMiddle().split(" ");
                User user = new User();
                user.setFirstName(separatedFullName[0]);
                user.setMiddleName(separatedFullName[1]);
                user.setLastName(separatedFullName[2]);
                user.setCreatedAt(now);
                user.setUpdatedAt(now);

                users.add(user);
            } while (users.size() % 1000 != 0);

            userRepository.saveAll(users);
            if (users.size() == 1000) {
                log.info("{} : Finished Row {}", name, userRepository.countAll());
            }
        }
    }
}
