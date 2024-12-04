package com.example.springboot.controller;

import com.example.springboot.auth.SecurityUtil;
import com.example.springboot.domain.account.Account;
import com.example.springboot.domain.account.AccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SecurityUtil securityUtil;
    private final AccountRepository accountRepository;

    public AuthController(final SecurityUtil securityUtil, final AccountRepository accountRepository) {
         this.securityUtil = securityUtil;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<Account> me() {
        final Account account = securityUtil.getManagedAccount();
        return ResponseEntity.ok(account);
    }

    // Example PUT Endpoint to change the currently authenticate users username
    @PutMapping("/username")
    public ResponseEntity<Account> username(@RequestParam String username) {
        final Account account = securityUtil.getManagedAccount();
        account.setUsername(username);
        final Account updatedAccount = accountRepository.saveAndFlush(account);
        return ResponseEntity.ok(updatedAccount);
    }

}
