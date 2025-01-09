package infra.config.jwt;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import infra.config.auth.PrincipalDetails;

@Component
public class JwtTokenProvider {

	private static final String SECRET_KEY = "your-secret-key";  // 비밀 키

    public String generateToken(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        
        // JWT 토큰 생성
        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withClaim("uid", principalDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))  // 1시간 만료
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }
}
