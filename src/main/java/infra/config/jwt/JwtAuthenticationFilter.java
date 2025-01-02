package infra.config.jwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import infra.config.auth.PrincipalDetails;
import infra.dto.UserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	
	 private final AuthenticationManager authenticationManager;

	 public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
	        this.authenticationManager = authenticationManager;
	    }
	 
	 
	 @Override
	 public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
	         throws AuthenticationException {
	     ObjectMapper objectMapper = new ObjectMapper();
	     UserDto userDto = null;
	     
	  // 요청 본문을 읽는 부분 (BufferedReader 사용)
	     StringBuilder sb = new StringBuilder();
	     String line;
	     try (BufferedReader reader = request.getReader()) {
	         while ((line = reader.readLine()) != null) {
	             sb.append(line);
	         }
	     } catch (IOException e) {
	         e.printStackTrace();
	         throw new IllegalArgumentException("Unable to read the request body.");
	     }

	     String requestBody = sb.toString();
	     System.out.println("Request Body: " + requestBody);  // 로그로 본문 확인

	     
	     try {
	         // 요청 본문을 UserDto로 파싱
	         userDto = objectMapper.readValue(requestBody, UserDto.class);
	         if (userDto == null || userDto.getUid() == null || userDto.getPassword() == null) {
	             throw new IllegalArgumentException("Invalid credentials provided.");
	         }
	     } catch (IOException e) {
	         e.printStackTrace();
	         throw new IllegalArgumentException("Unable to parse the request body to UserDto. Check if the request body is properly formatted and includes uid and password.");
	     }


	     // 인증 토큰 생성
	     UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
	             userDto.getUid(), userDto.getPassword());

	     return authenticationManager.authenticate(authenticationToken);
	 }

	 
	 @Override
	    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
	            FilterChain chain, Authentication authResult) throws IOException, ServletException {

	        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
	        String jwtToken = JWT.create()
	                .withSubject(principalDetails.getUsername())
	                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
	                .withClaim("uid", principalDetails.getUser().getUid())
	                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

	        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
	    }
	 
}
