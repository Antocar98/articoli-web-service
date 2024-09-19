package com.xantrix.webapp.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties("sicurezza")
@Data
public class JwtConfig
{
	private String header;
	private String secret;
}
