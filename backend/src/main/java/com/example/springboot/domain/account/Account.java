package com.example.springboot.domain.account;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.example.springboot.domain.AbstractEntity;
import com.example.springboot.domain.PublicView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Account extends AbstractEntity implements PublicView<Account.PublicAccount> {

    private String name; // via provider
    @Setter
    private String username; // updatable via app
    private String email;
    private String imageUrl;

    @Setter
    private boolean banned = false;

    @JsonManagedReference
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ConnectedAccount> connectedAccounts = new ArrayList<>();

    public String getUsername() {
        return StringUtils.isNotBlank(username) ? username : name;
    }

    public static Account fromOidc(DefaultOidcUser googleUser) {
        final Account account = new Account();
        account.name = googleUser.getFullName();
        account.email = googleUser.getEmail();
        account.imageUrl = googleUser.getPicture();
        return account;
    }

    public void addConnectedAccount(ConnectedAccount connectedAccount) {
        connectedAccounts.add(connectedAccount);
    }

    public PublicAccount toPublicView() {
        return new PublicAccount(
            getId(),
            getCreatedAt(),
            getUpdatedAt(),
            getUsername(),
            imageUrl,
            "publicaccount"
        );
    }

    public record PublicAccount(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        String name,
        String imageUrl,
        String type
    ) {
    }
}
