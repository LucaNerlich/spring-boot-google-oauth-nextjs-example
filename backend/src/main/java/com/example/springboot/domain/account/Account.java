package com.example.springboot.domain.account;

import com.example.springboot.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Account extends AbstractEntity {

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
}
