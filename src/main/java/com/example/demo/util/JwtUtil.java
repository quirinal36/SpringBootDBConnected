package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.exception.CommonException;
import com.example.demo.exception.EnumSecurityException;
import com.example.demo.model.JwtModel;
import com.example.demo.service.SolamonUserDetailsService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {
	public static final String ROLES_KEY = "roles";
	public static final String AUTHORITY_KEY = "AUTHORITY";
	
	public final String ACCESS_JWT_KEY;
	public final String REFRESH_JWT_KEY;
	public final long ACCESS_EXPIRE_MINUTES;
	public final long REFRESH_EXPIRE_MINUTES;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private SolamonUserDetailsService userDetailsService;
	
	public JwtUtil(@Value("${jwt.access.token.secure.key}")String ACCESS_KEY,
			@Value("${jwt.refresh.token.secure.key}")String REFRESH_KEY,
			@Value("${jwt.access.token.expire.time}")long aCCESS_EXPIRE_MINUTES,
			@Value("${jwt.refresh.token.expire.time}")long rEFRESH_EXPIRE_MINUTES) {
		super();
		this.ACCESS_JWT_KEY = ACCESS_KEY;
		this.REFRESH_JWT_KEY = REFRESH_KEY;
		this.ACCESS_EXPIRE_MINUTES = aCCESS_EXPIRE_MINUTES;
		this.REFRESH_EXPIRE_MINUTES = rEFRESH_EXPIRE_MINUTES;
	}
	
	public enum TOKEN_TYPE{
		ACCESS_TOKEN, REFRESH_TOKEN
	}
	
	// 토큰 타입 데이터
	private class TokenTypeData{
		
		private final String key;
		private final long time;
		
		public TokenTypeData(String key, long time) {
			super();
			this.key = key;
			this.time = time;
		}

		public String getKey() {
			return key;
		}

		public long getTime() {
			return time;
		}
	}
	
	/**
	 * 만료시간 추출
	 * 
	 * @param token
	 * @param tokenType
	 * @return
	 * @throws CommonException
	 */
	private Date extractExpiration(String token, TOKEN_TYPE tokenType) throws CommonException {
		return this.extractClaim(token, Claims::getExpiration, tokenType);
	}
	/**
	 * 토큰 파싱
	 * @param token
	 * @return
	 * @throws CommonException 
	 */
	private Claims extractAllClaims(String token, TokenTypeData ttd) throws CommonException {
		Claims body = null;
		try {
			body = Jwts.parser()
					.setSigningKey(ttd.getKey())
					.parseClaimsJws(token)
					.getBody();
		} catch (ExpiredJwtException e) {
			throw new CommonException(e, EnumSecurityException.ExpiredJwtException);
		} catch (UnsupportedJwtException e) {
			throw new CommonException(e, EnumSecurityException.UnsupportedJwtException);
		} catch (MalformedJwtException e) {
			throw new CommonException(e, EnumSecurityException.MalformedJwtException);
		} catch (SignatureException e) {
			throw new CommonException(e, EnumSecurityException.SignatureException);
		} catch (IllegalArgumentException e) {
			throw new CommonException(e, EnumSecurityException.IllegalArgumentException);
		}
		
		return body;
	}
	
	/**
	 * 토큰 만료 확인
	 * 
	 * @param token
	 * @return
	 * @throws CommonException 
	 */
	private Boolean isTokenExpired( String token,  TOKEN_TYPE tokenType) throws CommonException {
		
		Date date = this.extractExpiration( token, tokenType);
		return date.before( new Date());
	}
	
	/**
	 * 
	 * @param tokenType
	 * @return
	 */
	private TokenTypeData makeTokenTypeData( TOKEN_TYPE tokenType) {
		
		String key = tokenType==TOKEN_TYPE.ACCESS_TOKEN?this.ACCESS_JWT_KEY:this.REFRESH_JWT_KEY;
		long time = tokenType==TOKEN_TYPE.ACCESS_TOKEN?this.ACCESS_EXPIRE_MINUTES:this.REFRESH_EXPIRE_MINUTES;
		return new TokenTypeData(key, time);
	}
	/**
	 * 토큰 생성
	 * 
	 * @param claims
	 * @param subject
	 * @return
	 */
	private String createToken( Map<String, Object> claims, String subject, TOKEN_TYPE tokenType) {
		
		TokenTypeData ttd = this.makeTokenTypeData(tokenType);
		
		LocalDateTime d = LocalDateTime.now().plusMinutes( ttd.getTime());
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject( subject)
				.setExpiration( Date.from(d.atZone(ZoneId.systemDefault()).toInstant()))
				.setIssuedAt( Date.from( LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
				.signWith( SignatureAlgorithm.HS256, ttd.getKey())
				.compact();
	}

	/**
	 * 토큰 생성
	 * @param userDetails
	 * @return
	 */
	private JwtModel generateToken( UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<String, Object>();
		String accessToken = this.createToken(claims, userDetails.getUsername(), TOKEN_TYPE.ACCESS_TOKEN);
		String refreshToken = this.createToken(claims, userDetails.getUsername(), TOKEN_TYPE.REFRESH_TOKEN);
		redisUtil.setData(userDetails.getUsername(), refreshToken, REFRESH_EXPIRE_MINUTES);
		return new JwtModel(accessToken, refreshToken);
	}
	
	/**
	 * 토큰에서 사용자 이름 추출
	 * 
	 * @param token
	 * @return
	 * @throws CommonException 
	 */
	public String extractUsername( String token, TOKEN_TYPE tokenType) throws CommonException {
		String subject = this.extractClaim(token, Claims::getSubject, tokenType);
		return subject;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param token
	 * @param claimsResolver
	 * @param tokenType
	 * @return
	 * @throws CommonException
	 */
	public <T> T extractClaim( String token, Function<Claims, T> claimsResolver, TOKEN_TYPE tokenType) throws CommonException {
		
		TokenTypeData ttd = this.makeTokenTypeData(tokenType);
		
		final Claims claims = extractAllClaims( token , ttd);;
		return claimsResolver.apply( claims);
	}

	
	/**
	 * OAUTH
	 * @param username
	 * @param password
	 * @return
	 * @throws CommonException
	 */
	public JwtModel makeJwt( String username, String password) throws CommonException {
		try {
			this.authenticationManager
					.authenticate( 
						new UsernamePasswordAuthenticationToken( 
								username, 
								password));
			final UserDetails user = userDetailsService.loadUserByUsername(username);
			
			final JwtModel jwt = this.generateToken(user);
			
			return jwt;
		} catch ( BadCredentialsException e) {
			throw new CommonException(e, EnumSecurityException.BadCredentialsException);
		} catch (UsernameNotFoundException e) {
			throw new UsernameNotFoundException(username);
		} catch( InternalAuthenticationServiceException e) {
			throw new UsernameNotFoundException(username);
		}
		
	}
	
	/**
	 * 재발행
	 * @param username
	 * @return
	 * @throws CommonException
	 */
	public JwtModel makeReJwt() throws CommonException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(CommonUtil.empty(userDetailsService)) {
				log.info("userDetailsService empty");
			}
			if(CommonUtil.empty(authentication)) {
				log.info("authentication empty");
			}
			final UserDetails user = userDetailsService.loadUserByUsername(authentication.getName());
			
			final JwtModel jwt = this.generateToken(user);
			
			return jwt;
			
		}catch (BadCredentialsException e) {
			throw new CommonException(e, EnumSecurityException.BadCredentialsException);
		}
		
	}
	
	/**
	 * 토큰 검증
	 * 
	 * @param token
	 * @param userDetails
	 * @return
	 * @throws CommonException 
	 */
	public Boolean validateToken( String token, UserDetails userDetails, TOKEN_TYPE tokenType) throws CommonException {
		final String username = this.extractUsername(token, tokenType);
		return ( username.equals( userDetails.getUsername()) && !isTokenExpired(token, tokenType));
	}
}
