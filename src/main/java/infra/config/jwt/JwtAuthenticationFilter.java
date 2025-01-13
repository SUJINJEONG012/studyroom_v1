package infra.config.jwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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


// 로그인처리 담당하는 필터
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
	         throw new IllegalArgumentException("응답내용을 읽을 수 없다!! .");
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
	     
	     // 인증된 사용자 정보 가져오기
	     PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
	     
	     String jwtToken = JWT.create()
	             .withSubject(principalDetails.getUsername())
	             .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
	             .withClaim("uid", principalDetails.getUser().getUid())
	             .sign(Algorithm.HMAC512(JwtProperties.SECRET));
	     
	     // 응답 헤더에 JWT 토큰 추가
	     response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
	    
	     // 로그인 성공 메시지와 함께 응답, 응답 메시지 작성
	     Map<String, String> responseMessage = new HashMap<>();
	     responseMessage.put("message", "로그인 성공2333");
	     responseMessage.put("token", jwtToken);
	     responseMessage.put("redirect", "/"); // 리다이렉트 경로
	     
	     
	     // 응답 JSON 설정
	     response.setContentType("application/json;charset=UTF-8");
	     response.setCharacterEncoding("UTF-8");
	     
	    
	     
	     //json 형태로 응답
	     response.getWriter().write(new ObjectMapper().writeValueAsString(responseMessage));
	 }

	 
}
