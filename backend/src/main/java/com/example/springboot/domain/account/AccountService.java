package com.example.springboot.domain.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ConnectedAccountRepository connectedAccountRepository;

    public AccountService(final AccountRepository accountRepository, final ConnectedAccountRepository connectedAccountRepository) {
        this.accountRepository = accountRepository;
        this.connectedAccountRepository = connectedAccountRepository;
    }

    /**
     * Connects a account to a specified authentication provider by creating and saving
     * a new connected account for the account.
     *
     * @param provider the authentication provider to connect the account to
     * @param subject  the unique identifier for the account from the provider
     * @param account  the account to be connected to the provider
     * @return the updated account with the new connected account
     */
    public Account connect(String provider, String subject, Account account) {
        final ConnectedAccount newConnectedAccount = new ConnectedAccount(provider, subject, account);
        account.addConnectedAccount(newConnectedAccount);
        connectedAccountRepository.save(newConnectedAccount);
        return accountRepository.saveAndFlush(account);
    }

    /**
     * Retrieves a account by their email from the provided OIDC account details. If the account
     * does not exist, a new account is created based on the OIDC account details and saved
     * to the repository.
     *
     * @param oidcUser the OIDC account details used to find or create the account
     * @return the existing or newly created account
     */
    public Account getOrCreate(DefaultOidcUser oidcUser) {
        return accountRepository.findByEmail(oidcUser.getEmail())
            .orElseGet(() -> accountRepository.saveAndFlush(Account.fromOidc(oidcUser)));
    }
}
