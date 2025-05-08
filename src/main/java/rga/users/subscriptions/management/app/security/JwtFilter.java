package rga.users.subscriptions.management.app.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import rga.users.subscriptions.management.app.consts.Consts;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtProvider accessProvider;

    private String extractJwtFromRequest(HttpServletRequest servletRequest) {
        var authHeader = servletRequest.getHeader(Consts.AUTHORIZATION);
        return (StringUtils.hasText(authHeader) && authHeader.startsWith(Consts.BEARER))
                ? authHeader.substring(Consts.BEARER.length()) : null;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        final String jwt = extractJwtFromRequest((HttpServletRequest) servletRequest);

        if (jwt != null && accessProvider.validateJwt(jwt)) {

            final Claims jwtClaims = accessProvider.getJwtClaims(jwt);
            final CustomAuthentication jwtAuth = accessProvider.getJwtAuthentication(jwtClaims);

            jwtAuth.setAuthenticated(true);

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(jwtAuth);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
