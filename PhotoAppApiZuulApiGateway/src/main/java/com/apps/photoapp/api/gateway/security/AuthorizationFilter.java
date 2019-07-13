package com.apps.photoapp.api.gateway.security;

import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private static final String AUTHORIZATION_TOKEN_HEADER_PREFIX = "authorization.token.header.prefix";
    private static final String AUTHORIZATION_TOKEN_HEADER_PREFIX1 = "authorization.token.header.prefix";
    private static final String AUTHORIZATION_TOKEN_HEADER_NAME = "authorization.token.header.name";
    private static final String TOKEN_SECRET = "token.secret";
    private static final String AUTHORIZATION_TOKEN_HEADER_NAME1 = "authorization.token.header.name";
    private final Environment environment;

    public AuthorizationFilter(AuthenticationManager authManager, Environment environment) {
        super(authManager);
        this.environment = environment;
    }


    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain chain) throws IOException, ServletException {

        String authorizationHeader = request.getHeader(environment.getProperty(AUTHORIZATION_TOKEN_HEADER_NAME1));

        if (authorizationHeader == null || !authorizationHeader
                .startsWith(environment.getProperty(AUTHORIZATION_TOKEN_HEADER_PREFIX))) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(environment.getProperty(AUTHORIZATION_TOKEN_HEADER_NAME));

        if (authorizationHeader == null) {
            return null;
        }

        String token = authorizationHeader.replace(environment.getProperty(AUTHORIZATION_TOKEN_HEADER_PREFIX1), "");

        String userId = Jwts.parser().setSigningKey(environment.getProperty(TOKEN_SECRET)).parseClaimsJws(token)
                .getBody().getSubject();

        if (userId == null) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());

    }

}