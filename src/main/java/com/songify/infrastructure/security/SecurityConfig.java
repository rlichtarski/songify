package com.songify.infrastructure.security;

import com.songify.domain.usercrud.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(UserRepository userRepository) {
//        var manager = new InMemoryUserDetailsManager();
//        var user1 = User.withUsername("Radek")
//                .password("12345")
//                .roles("USER")
//                .build();
//        var user2 = User.withUsername("Smith")
//                .password("12345")
//                .roles("USER", "ADMIN")
//                .build();
//        manager.createUser(user1);
//        manager.createUser(user2);
//        return manager;
        return new UserDetailsServiceImpl(userRepository, passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
