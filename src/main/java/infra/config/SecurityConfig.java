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

	private final UserRepository userRepository;
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
    	.addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository))
    	.csrf(AbstractHttpConfigurer::disable)
    	.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    	.formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable);
//        .authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "MANAGER", "ADMIN")
//                .requestMatchers("/api/v1/manager/**").hasAnyRole("MANAGER", "ADMIN")
//                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
//                .anyRequest().permitAll()
//        );
		
//.csrf(csrf -> csrf.disable())
//				.authorizeHttpRequests(
//						auth -> auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 정적리소스
//																														// 혀용
//								.requestMatchers("/login", "/signup", "/assets/**").permitAll() // 로그인, 가입, 정적 자원 허용
//								.requestMatchers("/admin/**").hasRole("ADMIN")
//								// .requestMatchers("/**").permitAll()
//								.anyRequest().authenticated() // 모든 요청 인증 필요
//				)
//				// .formLogin(withDefaults()) // 기본로그인 사용
//				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/", true)
//						.failureUrl("/login?error=true").permitAll() // 로그인 페이지는 누구나 접근 가능
//				).logout(logout -> logout.logoutSuccessUrl("/")); // 로그아웃 성공 시 이동 경로

		return http.build();
	}

}
