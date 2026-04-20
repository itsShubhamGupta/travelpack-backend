package com.travelpack.config;

import com.travelpack.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
//@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TravelPackSecureConfig {

    @Autowired
    JwtAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // 🔥 IMPORTANT

//                .exceptionHandling(ex -> ex
//                        // 🔥 FIX HERE
//                        .authenticationEntryPoint((req, res, e) ->
//                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
//                        .accessDeniedHandler((req, res, e) ->
//                                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
//                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/signup", "/api/auth/signin").permitAll()
                        .anyRequest().authenticated()

                ).sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));




        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception
    {
        return builder.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}

