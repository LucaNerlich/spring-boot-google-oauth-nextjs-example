package com.example.springboot.domain.account;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.example.springboot.domain.AbstractEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
public class ConnectedAccount extends AbstractEntity {

    private String provider;
    private String subject;
    private Instant connectedAt;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @JsonBackReference
    private Account account;

    public ConnectedAccount(String provider, String subject, Account account) {
        this.provider = provider;
        this.subject = subject;
        this.connectedAt = Instant.now();
        this.account = account;
    }
}
