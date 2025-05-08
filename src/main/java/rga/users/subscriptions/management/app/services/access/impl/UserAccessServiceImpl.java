package rga.users.subscriptions.management.app.services.access.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rga.users.subscriptions.management.app.entities.Subscription;
import rga.users.subscriptions.management.app.enums.Role;
import rga.users.subscriptions.management.app.exceptions.AccessForbiddenException;
import rga.users.subscriptions.management.app.security.CustomAuthentication;
import rga.users.subscriptions.management.app.services.access.UserAccessService;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserAccessServiceImpl implements UserAccessService {

    @Override
    public boolean isAdmin() {
        return getAuthenticatedUser()
                .map(
                        principal -> isRole(principal.getAuthorities(), Role.ROLE_ADMIN.getValue())
                )
                .orElse(false);
    }

    @Override
    public boolean isSubscriber(Subscription subscription) {
        return getAuthenticatedUser()
                .map(
                        principal ->
                                subscription.getUser() != null && isSubscriber(subscription, principal)
                )
                .orElse(false);
    }

    @Override
    public String getCurrentEmail() {
        return getAuthenticatedUser()
                .map(CustomAuthentication::getEmail)
                .orElseThrow(() -> new AccessForbiddenException(HttpStatus.FORBIDDEN,
                        "Access denied. Please authenticate first."));
    }

    private boolean isRole(Collection<? extends GrantedAuthority> authorities, String role) {
        return authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role::equals);
    }

    private Optional<CustomAuthentication> getAuthenticatedUser() {
        return Optional.ofNullable(
                (CustomAuthentication) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );
    }

    private boolean isSubscriber(Subscription subscription, CustomAuthentication principal) {
        return subscription.getUser().getEmail().equals(principal.getEmail());
    }

}
