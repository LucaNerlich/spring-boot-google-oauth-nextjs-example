package com.example.springboot.auth;

import com.example.springboot.config.ConfigService;
import com.example.springboot.domain.account.Account;
import com.example.springboot.domain.account.AccountService;
import com.example.springboot.domain.account.ConnectedAccount;
import com.example.springboot.domain.account.ConnectedAccountRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AccountService accountService;
    private final ConfigService configService;
    private final ConnectedAccountRepository connectedAccountRepository;

    public Oauth2LoginSuccessHandler(final AccountService accountService, final ConfigService configService, final ConnectedAccountRepository connectedAccountRepository) {
        this.accountService = accountService;
        this.configService = configService;
        this.connectedAccountRepository = connectedAccountRepository;
    }

    /**
     * Handles a successful authentication event.
     *
     * @param request        the HTTP servlet request containing authentication details.
     * @param response       the HTTP servlet response to handle any resulting actions.
     * @param authentication the Authentication object containing account authentication info.
     * @throws IOException      if an input or output exception occurs.
     * @throws ServletException if a servlet-specific exception occurs.
     */
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("Authentication: {}", authentication);
        final DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        final OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        final String provider = authenticationToken.getAuthorizedClientRegistrationId();
        final String subject = oidcUser.getName();

        // Find existing and connected account
        final Optional<ConnectedAccount> connectedAccount = connectedAccountRepository.findByProviderAndSubject(provider, subject);
        if (connectedAccount.isPresent()) {
            final Account account = connectedAccount.get().getAccount();
            authenticate(account, response);
            return;
        }

        // Create and connect new account
        final Account account = accountService.getOrCreate(oidcUser);
        final Account connectedUser = accountService.connect(provider, subject, account);
        authenticate(connectedUser, response);
    }

    /**
     * Authenticates the given account and redirects the response to the home page.
     *
     * @param account  the account to be authenticated
     * @param response the HTTP servlet response to handle the redirection
     * @throws IOException if an input or output exception occurs
     */
    private void authenticate(Account account, HttpServletResponse response) throws IOException {
        final AppAuthenticationToken token = new AppAuthenticationToken(account);
        SecurityContextHolder.getContext().setAuthentication(token);
        response.sendRedirect(configService.getFrontendUrl());
    }

}
