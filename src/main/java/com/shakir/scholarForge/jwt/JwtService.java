package com.shakir.scholarForge.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	private static final String SECRET_KEY="Your _Secret_key";
	
	public String extractUsername(String token) {
		// TODO Auto-generated method stub
		return extractClaims(token,Claims::getSubject);
	}
	public String generateTokenForUser(Map<String,Object> extractClaims,UserDetails userDetails) {
		return Jwts.builder().setClaims(extractClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
				.signWith(getSigninKey(),SignatureAlgorithm.HS256)
				.compact();
	}
	public boolean isTokenValid(String token,UserDetails userDetails) {
		String username=extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
	public boolean isTokenExpired(String token) {
		// TODO Auto-generated method stub
		return extractExpiration(token).before(new Date());
	}
	public Date extractExpiration(String token) {
		// TODO Auto-generated method stub
		return extractClaims(token,Claims::getExpiration);
	}
	public <T> T extractClaims(String token,Function <Claims,T> claimResolver) {
		final Claims claims=extractAllClaims(token);
		return claimResolver.apply(claims);
		
	}
	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigninKey())
				.build().parseClaimsJws(token).getBody();
				
	}
	private Key getSigninKey() {
		byte[] keyBytes=Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
