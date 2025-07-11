package org.exp.primeapp.configs.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.exp.primeapp.utils.Const.*;

@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http, MySecurityFilter mySecurityFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(
                                "/index.html",
                                "/index.html/**"
                        ).permitAll()
                        .requestMatchers(
                                "/pages",
                                "/pages/**"
                        ).permitAll()

                        .requestMatchers(
                               API + V1 + AUTH,
                               API + V1 + AUTH + WAY_ALL
                        ).permitAll()

                       .requestMatchers(
                               "GET",
                               API + V1 + PRODUCT,
                               API + V1 + PRODUCT + WAY_ALL
                       ).permitAll()

                       .requestMatchers(
                               "GET",
                               API + V1 + PRODUCTS,
                               API + V1 + PRODUCTS + BY_CATEGORY + "/{categoryId}",
                               API + V1 + PRODUCTS + WAY_ALL
                       ).permitAll()

                       .requestMatchers(
                               "GET",
                               API + V1 + CATEGORY,
                               API + V1 + CATEGORY + WAY_ALL
                       ).permitAll()

                       .requestMatchers(
                               "GET",
                               API + V1 + CATEGORIES,
                               API + V1 + CATEGORIES + WAY_ALL
                       ).permitAll()

                       .requestMatchers(
                               "GET",
                               API + V1 + ATTACHMENT + WAY_ALL
                       ).permitAll()

                        .requestMatchers(
                                "GET",
                                API + V1 + SPOTLIGHTS + WAY_ALL
                        ).permitAll()

                        .anyRequest().permitAll()
        );

        http.addFilterBefore(mySecurityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost/", "http://localhost/**")); // IDE server porti
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // Agar cookie yoki token ishlatilsa
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(customUserDetailsService);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }
}