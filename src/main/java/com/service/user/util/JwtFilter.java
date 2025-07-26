package com.service.user.util;

import com.service.user.service.JwtService;
import com.service.user.service.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String token  = null;
        String email = null;
        UUID userId = null;
        try{
            if(authorization != null && authorization.startsWith("Bearer ")){
                token = authorization.substring(7);
                email = jwtService.extractUserName(token);
                userId = jwtService.extractUserId(token);
            }
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = applicationContext.getBean(MyUserDetailService.class).loadUserByUsername(email);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            request.setAttribute("userId", userId);
            filterChain.doFilter(request, response);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("---------------------------------------------------------------------");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Session expired. Please login again.\"}");
            System.out.println("Expired JWT: " + e.getMessage());

        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Invalid token.\"}");
            System.out.println("Invalid JWT: " + e.getMessage());
        }
    }
}
