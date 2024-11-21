package com.example.springboot.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectedAccountRepository extends JpaRepository<ConnectedAccount, Long> {

    @Query("SELECT a FROM ConnectedAccount a WHERE a.provider = :provider AND a.subject = :subject")
    Optional<ConnectedAccount> findByProviderAndSubject(@Param("provider") String provider, @Param("subject") String subject);

}
