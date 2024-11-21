package com.example.springboot.auth;

import com.example.springboot.ApiException;
import com.example.springboot.domain.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    /**
     * Retrieves the authenticated User from the SecurityContextHolder.
     *
     * @return the authenticated User.
     * @throws ApiException if no authenticated account is found in the SecurityContextHolder.
     */
    public static Account getAuthenticatedUser() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Account account) {
            return account;
        } else {
            log.error("User requested but not found in SecurityContextHolder");
            throw ApiException.builder().status(401).message("Authentication required").build();
        }
    }
}
