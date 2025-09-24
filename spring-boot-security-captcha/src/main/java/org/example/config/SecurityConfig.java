package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requestMatcherRegistry -> {
            requestMatcherRegistry.requestMatchers("/captcha/get").permitAll();
            requestMatcherRegistry.anyRequest().authenticated();
        } );

        http.formLogin(loginConfigurer -> {

        });

        return http.build();
    }

}
