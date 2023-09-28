package com.example.physioclinic.security;

import com.example.physioclinic.service.AppService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider(AppService appService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(appService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select username, password, enabled from users where username=?"
        );

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT usr.username, authority.authority " +
                        "FROM users usr, authorities authority " +
                        "WHERE usr.username = ? " +
                        "and usr.user_id = authority.user_id"
        );
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/v3/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/physiotherapist").hasRole("PHYSIOTHERAPIST")
                        .requestMatchers(HttpMethod.GET, "/patient").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/logged_username").authenticated()
                        .requestMatchers(HttpMethod.GET, "/logged_authorities").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/disable_user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/change_password").authenticated()
                        .requestMatchers(HttpMethod.POST, "/new_patient").anonymous()
                        .requestMatchers(HttpMethod.POST, "/new_physiotherapist").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/add_disease").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/update_disease").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/display_disease").authenticated()
                        .requestMatchers(HttpMethod.POST, "/add_diagnosis").hasRole("PHYSIOTHERAPIST")
                        .requestMatchers(HttpMethod.GET, "/list_of_my_diagnosis").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/list_of_patients_diagnosis").hasRole("PHYSIOTHERAPIST")
                        .requestMatchers(HttpMethod.GET, "/exercises_picture").authenticated()
                        .requestMatchers(HttpMethod.GET, "/exercises_list").hasRole("PHYSIOTHERAPIST")
                        .requestMatchers(HttpMethod.GET, "/exercises_for_patients_diagnosis").authenticated());

        http.httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
