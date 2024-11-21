package com.example.springboot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Service
public class ConfigService {

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    @Value("${NEXT_PUBLIC_URL}")
    private String frontendUrl;

    @Value("${COOKIE_DOMAIN:localhost}")
    private String cookieDomain;

}
