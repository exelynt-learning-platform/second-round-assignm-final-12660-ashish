package com.ecommerce.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository repo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // ✅ IMPORTANT: allow requests without token (like Stripe webhook)
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {

            if (jwtUtil.validateToken(token)) {

                String username = jwtUtil.extractUsername(token);

                User user = repo.findByUsername(username)
                        .orElse(null);

                if (user != null) {

                    CustomUserDetails userDetails =
                            new CustomUserDetails(user);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

        } catch (Exception e) {
            // ❗ DO NOT BLOCK REQUEST
            e.printStackTrace();
        }

        // ✅ Always continue filter chain
        chain.doFilter(request, response);
    }
}