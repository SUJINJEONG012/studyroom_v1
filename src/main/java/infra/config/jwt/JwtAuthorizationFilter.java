package infra.config.jwt;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import infra.config.auth.PrincipalDetails;
import infra.entity.User;
import infra.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


//인증, 인가 실행될때 자동으로 호출되는 필터
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final  UserRepository userRepository;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
	    
	    // 헤더에서 Authorization 키 확인
	    String header = request.getHeader(JwtProperties.HEADER_STRING);
	    
	    if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
	        chain.doFilter(request, response);
	        return;
	    }
	    
	    // Authorization 헤더에서 토큰 추출
	    String token = header.replace(JwtProperties.TOKEN_PREFIX, "");

	    try {
	        // JWT 토큰 검증
	        String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
	                .build()
	                .verify(token)
	                .getClaim("uid")
	                .asString();
	        
	        if (username != null) {
	            // DB에서 유저 조회
	            User user = userRepository.findByUid(username)
	                    .orElseThrow(() -> new RuntimeException("User not found"));
	            
	            // PrincipalDetails 객체 생성
	            PrincipalDetails principalDetails = new PrincipalDetails(user);
	            
	            // 인증 객체 생성
	            Authentication authentication = new UsernamePasswordAuthenticationToken(
	                    principalDetails, 
	                    null, 
	                    principalDetails.getAuthorities()
	            );
	            
	            // SecurityContext에 인증 객체 설정
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	        }
	    } catch (Exception e) {
	        // 토큰이 유효하지 않거나 만료된 경우 403 Forbidden 상태 코드 반환
	        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	        response.getWriter().write("Forbidden: Invalid or expired token");
	        return;
	    }

	    // 필터 체인 계속 진행
	    chain.doFilter(request, response);
	}

	
}
