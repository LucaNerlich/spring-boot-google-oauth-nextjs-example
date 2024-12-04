package com.example.springboot.auth;

import com.example.springboot.ApiException;
import com.example.springboot.domain.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@Component
public class SecurityUtil {

    private final AccountRepository accountRepository;

    public SecurityUtil(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Retrieves the authenticated User from the SecurityContextHolder.
     *
     * @return the authenticated User.
     * @throws ApiException if no authenticated account is found in the SecurityContextHolder.
     */
    private Account getAuthenticatedUser() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Account account) {
            return account;
        } else {
            log.error("User requested but not found in SecurityContextHolder");
            throw ApiException.builder().status(401).message("Authentication required").build();
        }
    }

    /**
     * Retrieves the authenticated and managed Account entity of the current user from the database.
     *
     * This method first obtains the authenticated account through the security context holder.
     * If the account is not authenticated, an ApiException is thrown with a status code 401,
     * indicating that authentication is required. If the authenticated account is not found
     * in the AccountRepository, another ApiException is thrown with the same status code,
     * indicating that the authenticated user was not found in the database.
     *
     * @return the authenticated and managed Account from the database.
     * @throws ApiException if authentication is required or if the authenticated account is not found.
     */
    public Account getManagedAccount() {
        final Account account = getAuthenticatedUser();
        if (account == null) {
            throw ApiException.builder().status(401).message("Authentication required").build();
        }
        return accountRepository.findById(account.getId())
            .orElseThrow(() -> ApiException.builder().status(401).message("Authenticated user not found in database").build());
    }
}
