package com.learn.ecommerce.security.jwt;

import com.learn.ecommerce.security.service.UsersDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${jwt.expirationMs}")
	private Integer jwtExpirationMs;

	@Value("${jwt.refreshExpirationMs}")
	private Integer jwtRefreshExpirationMs;

	@Value("${jwt.secret}")
	private String jwtSecret;

	public boolean validateJwtToken ( String authToken ) {
		try {
			Jwts.parser().setSigningKey(jwtSecret)
					.parseClaimsJws(authToken);

			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT Signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT Token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT Token expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT Token is Unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT Token is Illegal: {}", e.getMessage());
		}

		return false;
	}

	public String generateJwtToken ( Authentication authentication ) {
		UsersDetailsImpl principal = (UsersDetailsImpl) authentication.getPrincipal();
		return Jwts.builder().setSubject((principal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public String generatRefreshJwtToken ( Authentication authentication ) {
		UsersDetailsImpl principal = (UsersDetailsImpl) authentication.getPrincipal();
		return Jwts.builder().setSubject((principal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public String getUsernameFromJwtToken ( String token ) {
		return Jwts.parser().setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
}