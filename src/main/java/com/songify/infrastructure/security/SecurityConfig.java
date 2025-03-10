package com.songify.infrastructure.security;

import com.songify.domain.usercrud.UserRepository;
//import com.songify.infrastructure.security.jwt.JwtAuthTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository, passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // SOME PARTS ARE COMMENTED OUT BECAUSE OF OAUTH2 FOR GOOGLE
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity/*, JwtAuthTokenFilter jwtAuthTokenFilter*/) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(corsConfigurerCustomizer());
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.oauth2Login(Customizer.withDefaults());
//        httpSecurity.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        httpSecurity.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/users/register/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/songs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/artists/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/albums/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/genres/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/token/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/songs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/songs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/songs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/songs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/artists/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/artists/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/artists/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/artists/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/albums/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/albums/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/genres/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated());
        return httpSecurity
                .build();
    }

    public Customizer<CorsConfigurer<HttpSecurity>> corsConfigurerCustomizer() {
        return c -> {
            CorsConfigurationSource source = request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(
                        List.of("https://localhost:3000"));
                config.setAllowedMethods(
                        List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);
                return config;
            };
            c.configurationSource(source);
        };
    }

}
