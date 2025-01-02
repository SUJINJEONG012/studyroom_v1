package infra.config.jwt;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

	private final UserRepository userRepository;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// 헤더에 Authorizaion 키 여부 확인
		String header = request.getHeader(JwtProperties.HEADER_STRING);
		if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		// 키가 존재한다면 -> UserDetailsService의 loadByUsername.... 메소드로 토큰 검증(인증)
		String token = request.getHeader(JwtProperties.HEADER_STRING)
								.replace(JwtProperties.TOKEN_PREFIX, "");
		
		
		String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
								.getClaim("uid").asString();
		
		if (username != null) {
			// Optional에서 User 객체를 꺼내기
						User user = userRepository.findByUid(username)
			                                      .orElseThrow(() -> new RuntimeException("User not found"));

			// 인가
			PrincipalDetails principalDetails = new PrincipalDetails(user);
			Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, 
																					null, 
																					principalDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		chain.doFilter(request, response);
	}
	
}
