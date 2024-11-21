package com.example.springboot.auth;

import com.example.springboot.domain.account.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.io.Serial;
import java.util.Collection;

public class AppAuthenticationToken implements Authentication {

    @Serial
    private static final long serialVersionUID = 2641880265803745820L;

    private final Account account;
    private final boolean authenticated;
    private Collection<GrantedAuthority> authorities;
    private WebAuthenticationDetails details;

    public AppAuthenticationToken(Account account) {
        this.account = account;
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Account getPrincipal() {
        return account;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use constructor to set authenticated to true");
    }

    @Override
    public String getName() {
        return account.getName();
    }

}
