package kr.co.lunasoft.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.co.lunasoft.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenService {

	private static final String SECREY_KEY = "ThisIsLunaCoreDeanJWTProject";
	private static final long TTL_MILLIS = 1000 * 60 * 1; // 1분

	public UserInfo compare(String id, String pwd) {
		UserInfo userInfo = new UserInfo();
		if (id.equals("jhlee") && pwd.equals("1234")) {
			userInfo.setId(id);
			userInfo.setName("이진형");
			userInfo.setRole("ADMIN");
		} else if (id.equals("dean") && pwd.equals("1234")) {
			userInfo.setId(id);
			userInfo.setName("딘딘딘");
			userInfo.setRole("MANAGER");
		} else {
			userInfo = null;
		}
		return userInfo;
	}

	public String sign(UserInfo userInfo) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		Date expireTime = new Date();
		expireTime.setTime(expireTime.getTime() + TTL_MILLIS);
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECREY_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		Map<String, Object> headerMap = new HashMap<String, Object>();
		headerMap.put("alg", "HS256");
		headerMap.put("typ", "JWT");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userInfo.getId());
		map.put("name", userInfo.getName());
		map.put("role", userInfo.getRole());

		JwtBuilder builder = Jwts.builder().setHeader(headerMap).setClaims(map).setExpiration(expireTime).signWith(signatureAlgorithm, signingKey);

		return builder.compact();
	}

	public boolean verify(String token) {
		try {
			// 정상 수행된다면 해당 토큰은 정상토큰
			Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECREY_KEY)).parseClaimsJws(token).getBody();

			log.info("expireTime :" + claims.getExpiration());
			log.info("id : " + claims.get("id"));
			log.info("name : " + claims.get("name"));
			log.info("role : " + claims.get("role"));
			return true;
		} catch (ExpiredJwtException exception) {
			log.error("토큰 만료");
			return false;
		} catch (JwtException exception) {
			log.error("토큰 변조");
			return false;
		}
	}

}
