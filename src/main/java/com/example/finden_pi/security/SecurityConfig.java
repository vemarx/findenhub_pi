package com.example.finden_pi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 🔓 Páginas e recursos públicos
                        .requestMatchers(
                                "/", // ✅ raiz
                                "/index.html", // ✅ arquivo direto
                                "/register",
                                "/login",
                                "/services",
                                "/services/category/**",
                                "/services/{id:[\\w-]+}",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/fonts/**",
                                "/assets/**")
                        .permitAll()

                        // 🔐 Áreas protegidas
                        .requestMatchers("/organizer/**").hasAuthority("ORGANIZER")
                        .requestMatchers("/supplier/**").hasAuthority("SUPPLIER")
                        .requestMatchers("/", "/register", "/login", "/css/**", "/js/**", "/images/**",
                                "/api/locations/**")
                        .permitAll()
                        .requestMatchers("/", "/register", "/login", "/css/**", "/js/**", "/images/**",
                                "/api/locations/**")
                        .permitAll()
                        .requestMatchers("/api/upload/**").authenticated()

                        // 🔒 Tudo o resto exige login
                        .anyRequest().authenticated())

                // 🧭 Configuração de login
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll())

                // 🚪 Configuração de logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll())

                // 🧩 Ignora CSRF só para APIs
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
