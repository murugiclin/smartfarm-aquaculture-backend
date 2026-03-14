package com.smartfarm.aquaculture.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.smartfarm.aquaculture.repository")
@EnableJpaAuditing
public class JpaConfig {
}
