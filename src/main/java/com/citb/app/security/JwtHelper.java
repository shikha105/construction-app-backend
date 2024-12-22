package com.citb.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtHelper {

	@Value("${application.security.jwt.secret-key}")
	private String secretKey;
	@Value("${application.security.jwt.expiration}")
	private long jwtExpiration;
	@Value("${application.security.jwt.refresh-token.expiration}")
	private long refreshExpiration;

	public String extractUsername(String token) {

		String username = extractClaim(token, Claims::getSubject);
		if (username == null) {
			System.out.println("Username is null");
		}
		return username;
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);

		return claimsResolver.apply(claims);
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	public String generateRefreshToken(UserDetails userDetails) {
		return buildToken(new HashMap<>(), userDetails, refreshExpiration);
	}

	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {

		Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();

		return claims;
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}

/*
 * old code
 * 
 * @Component public class JwtHelper {
 * 
 * public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
 * 
 * private String secret =
 * "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
 * 
 * public String getUsernameFromToken(String token) { return
 * getClaimFromToken(token, Claims::getSubject); }
 * 
 * public Date getExpirationDateFromToken(String token) { return
 * getClaimFromToken(token, Claims::getExpiration); }
 * 
 * public <T> T getClaimFromToken(String token, Function<Claims, T>
 * claimsResolver) { final Claims claims = getAllClaimsFromToken(token); return
 * claimsResolver.apply(claims); }
 * 
 * public Claims getAllClaimsFromToken(String token) {
 * 
 * Claims claims =
 * Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
 * System.out.println(claims +" claims from get all claims"); return claims; }
 * 
 * // private Key getSignInKey() { // byte[] keyBytes =
 * Decoders.BASE64.decode(secret); // return Keys.hmacShaKeyFor(keyBytes); // }
 * 
 * 
 * private Boolean isTokenExpired(String token) { final Date expiration =
 * getExpirationDateFromToken(token); return expiration.before(new Date()); }
 * 
 * 
 * public String generateToken(UserDetails userDetails) { Map<String, Object>
 * claims = new HashMap<>();
 * 
 * return doGenerateToken(claims, userDetails.getUsername());
 * 
 * }
 * 
 * 
 * private String doGenerateToken(Map<String, Object> claims, String subject) {
 * 
 * return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new
 * Date(System.currentTimeMillis())) .setExpiration(new
 * Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
 * .signWith(SignatureAlgorithm.HS512, secret).compact(); }
 * 
 * 
 * public Boolean validateToken(String token, UserDetails userDetails) { final
 * String username = getUsernameFromToken(token);
 * 
 * System.out.println("username token se jo mila:  " + username +
 * "userdetails se jo mila"+ userDetails.getUsername()) ;
 * System.out.println("token expire hua kya?"+ isTokenExpired(token)); return
 * (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); }
 * 
 * }
 */
