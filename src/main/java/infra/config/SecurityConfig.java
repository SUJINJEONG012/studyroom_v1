package infra.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import infra.config.jwt.JwtAuthenticationFilter;
import infra.config.jwt.JwtAuthorizationFilter;
import infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final UserDetailsService userDetailsService;
	private final CorsConfig corsConfig;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}



	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager ) throws Exception {
		http
    	// 로그인 시 JWT 토큰 발급을 위한 필터 추가 (JwtAuthenticationFilter)
    	.addFilter(new JwtAuthenticationFilter(authenticationManager))
    	
    	// 요청 시 JWT 토큰을 검증하는 필터 추가 (JwtAuthorizationFilter)
    	.addFilter(new JwtAuthorizationFilter(authenticationManager, userDetailsService))
    	.csrf(AbstractHttpConfigurer::disable)
    	.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    	.formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable);
    	

		return http.build();
	}

}
