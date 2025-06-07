package com.example.account_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        String requestPath = request.getRequestURI();

        // Ignorer les endpoints publics (Swagger, actuator, etc.)
        if (isPublicEndpoint(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                // Token valide, extraire les informations
                String username = claims.getSubject();
                Object userId = claims.get("userId");
                String role = claims.get("role", String.class);
                Date expiration = claims.getExpiration();
                Date issuedAt = claims.getIssuedAt();

                // Vérifier si le token n'est pas expiré (double vérification)
                if (expiration != null && expiration.before(new Date())) {
                    handleErrorResponse(response, HttpStatus.UNAUTHORIZED, 
                            "Token expired", "The JWT token has expired. Please login again.");
                    return;
                }

                // Ajouter les informations utilisateur aux attributs de la requête
                request.setAttribute("username", username);
                request.setAttribute("userId", userId);
                request.setAttribute("role", role);
                request.setAttribute("jwt", jwt);
                request.setAttribute("issuedAt", issuedAt);
                request.setAttribute("expiration", expiration);

                System.out.println("Token validated for user: " + username + 
                                 " (ID: " + userId + ", Role: " + role + ")");

                // Continuer la chaîne de filtres avec un token valide
                filterChain.doFilter(request, response);
                return;

            } catch (ExpiredJwtException e) {
                System.err.println("Token expired: " + e.getMessage());
                handleErrorResponse(response, HttpStatus.UNAUTHORIZED, 
                        "Token expired", "The JWT token has expired. Please login again.");
                return;

            } catch (MalformedJwtException e) {
                System.err.println("Invalid JWT token format: " + e.getMessage());
                handleErrorResponse(response, HttpStatus.UNAUTHORIZED, 
                        "Invalid token", "The JWT token is malformed.");
                return;

            } catch (UnsupportedJwtException e) {
                System.err.println("Unsupported JWT token: " + e.getMessage());
                handleErrorResponse(response, HttpStatus.UNAUTHORIZED, 
                        "Unsupported token", "The JWT token format is not supported.");
                return;

            } catch (IllegalArgumentException e) {
                System.err.println("JWT claims string is empty: " + e.getMessage());
                handleErrorResponse(response, HttpStatus.UNAUTHORIZED, 
                        "Invalid token", "The JWT token is empty or invalid.");
                return;

            } catch (Exception e) {
                System.err.println("Token validation error: " + e.getMessage());
                handleErrorResponse(response, HttpStatus.UNAUTHORIZED, 
                        "Invalid token", "An error occurred while validating the token.");
                return;
            }
        }

        // Pas de token Bearer, vérifier si l'endpoint nécessite une authentification
        if (requiresAuthentication(requestPath)) {
            handleErrorResponse(response, HttpStatus.UNAUTHORIZED, 
                    "Missing token", "Authorization token is required for this endpoint.");
            return;
        }

        // Continuer sans token pour les endpoints publics
        filterChain.doFilter(request, response);
    }

    /**
     * Vérifie si l'endpoint est public (ne nécessite pas d'authentification)
     */
    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/actuator") ||
               path.equals("/favicon.ico") ||
               path.startsWith("/public") ||
               path.startsWith("/health");
    }

    /**
     * Vérifie si l'endpoint nécessite une authentification
     */
    private boolean requiresAuthentication(String path) {
        return path.startsWith("/api/accounts") ||
               path.startsWith("/api/protected");
    }

    /**
     * Méthode pour envoyer une réponse d'erreur formatée
     */
    private void handleErrorResponse(HttpServletResponse response, HttpStatus status,
                                     String error, String message) throws IOException {
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", new Date());
        errorResponse.put("status", status.value());
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("path", "JWT Authentication Filter");

        // Configurer la réponse HTTP
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Ajouter les en-têtes CORS si nécessaire
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");

        // Écrire le JSON dans la réponse
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);

        System.err.println("JWT Error Response: " + errorResponse);
    }

    /**
     * Méthode utilitaire pour extraire les informations utilisateur depuis la requête
     */
    public static String getUsernameFromRequest(HttpServletRequest request) {
        return (String) request.getAttribute("username");
    }

    public static Object getUserIdFromRequest(HttpServletRequest request) {
        return request.getAttribute("userId");
    }

    public static String getRoleFromRequest(HttpServletRequest request) {
        return (String) request.getAttribute("role");
    }

    public static String getJwtFromRequest(HttpServletRequest request) {
        return (String) request.getAttribute("jwt");
    }
}