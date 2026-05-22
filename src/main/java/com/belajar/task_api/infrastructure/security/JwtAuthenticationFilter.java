package com.belajar.task_api.infrastructure.security;

// 1. PASTIKAN SEMUA IMPORT DI BAWAH INI ADA DAN MENGGUNAKAN JAKARTA
import jakarta.servlet.FilterChain; // <-- Ini yang tadi hilang dan menyebabkan error abstract
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Periksa apakah request membawa header Authorization berbentuk 'Bearer <token>'
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Potong teks "Bearer "

            if (jwtUtil.validateToken(token)) {
                // PERBAIKAN: Ditambah titik koma (;) di ujung baris
                String username = jwtUtil.getUsernameFromToken(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Masukkan data user ke context Spring Security
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // Lanjutkan request ke proses berikutnya
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }
}