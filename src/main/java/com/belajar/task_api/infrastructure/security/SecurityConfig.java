package com.belajar.task_api.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // <-- 1. TAMBAHKAN INI agar Lombok otomatis meng-inject JwtAuthenticationFilter
public class SecurityConfig {

    // 2. SUNTIKKAN Filter JWT yang sudah kita perbaiki tadi
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                // 3. TAMBAHKAN INI: Set session menjadi STATELESS (Karena kita pakai JWT, bukan HTTP Session/Cookie)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 4. UBAH DI SINI: Izinkan endpoint login diakses tanpa token
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // Jika Anda pakai Swagger, buka juga pintu Swagger-nya di sini:
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // 5. UBAH DI SINI: Semua request API selain login WAJIB diautentikasi (punya token)
                        .anyRequest().authenticated()
                )

                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // 6. TAMBAHKAN INI: Taruh JwtAuthenticationFilter kita SEBELUM filter bawaan Spring Security
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}