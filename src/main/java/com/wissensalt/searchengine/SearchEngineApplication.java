package com.wissensalt.searchengine;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.util.StopWatch;

@EnableElasticsearchRepositories(basePackages = "com.wissensalt.searchengine.es")
@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class SearchEngineApplication implements CommandLineRunner {

	@Value("${app.seeding.enabled}")
	private boolean isSeedingEnabled;

	private final Seeder seeder;

	public static void main(String[] args) {
		SpringApplication.run(SearchEngineApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (isSeedingEnabled) {
			log.info("start seeding");
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			seeder.seed();
			stopWatch.stop();
			log.info("Finished seeding in {} milliseconds", stopWatch.getTotalTimeMillis());
		}
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Search Engine"));
	}
}
