package com.academic.fh.config;

import com.academic.fh.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final CustomUserDetailsService userDetailsService;

        public SecurityConfig(CustomUserDetailsService userDetailsService) {
                this.userDetailsService = userDetailsService;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                // Rutas públicas
                                                .requestMatchers("/", "/productos/**", "/login", "/register", "/error",
                                                                "/mi-cuenta")
                                                .permitAll()
                                                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**",
                                                                "/img/**", "/uploads/**")
                                                .permitAll()
                                                .requestMatchers("/h2-console/**").permitAll()

                                                // Rutas de admin
                                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                                // Rutas de cliente
                                                .requestMatchers("/cliente/**", "/cart/**", "/checkout/**")
                                                .hasRole("USER")

                                                // Cualquier otra ruta requiere autenticación
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/login-success", true)
                                                .failureUrl("/login?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID", "remember-me")
                                                .permitAll())
                                .rememberMe(remember -> remember
                                                .key("uniqueAndSecretKey")
                                                .tokenValiditySeconds(86400 * 30) // 30 días
                                                .rememberMeParameter("remember-me")
                                                .userDetailsService(userDetailsService))
                                .sessionManagement(session -> session
                                                .maximumSessions(1)
                                                .maxSessionsPreventsLogin(false))
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/h2-console/**"))
                                .headers(headers -> headers
                                                .frameOptions(frame -> frame.sameOrigin()));

                return http.build();
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

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
