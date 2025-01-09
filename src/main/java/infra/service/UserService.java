package infra.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import infra.config.jwt.JwtTokenProvider;
import infra.dto.UserDto;
import infra.entity.User;
import infra.entity.constant.UserRoleType;
import infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	 private final AuthenticationManager authenticationManager;
	    private final JwtTokenProvider jwtTokenProvider;
	
	public String authenticateUser(UserDto userDto) {
        try {
            // 사용자 인증 (예: UsernamePasswordAuthenticationToken 사용)
            UsernamePasswordAuthenticationToken authenticationToken = 
                    new UsernamePasswordAuthenticationToken(userDto.getUid(), userDto.getPassword());
            
            // 인증 매니저로 인증을 시도
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            
            // 인증 성공 시 JWT 토큰 발급
            String token = jwtTokenProvider.generateToken(authentication);
            return token;
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
	
	
	public void registerUser(UserDto userDto) {
		
//		if(userRepository.existsById(userDto.getUid())) {
//			throw new RuntimeException("이미 있는 아이디 입니다.");
//		}
		// 비즈니스로직에 따라 roleType 설정
		if(userDto.getBusinessNum() != null) {
			// 비즈니스로직이 있다면 HOST
			userDto.setRoleType(UserRoleType.HOST);
		}else {
			// 없으면 GUEST
			userDto.setRoleType(UserRoleType.GUEST);
		}
		
		
		//암호화추가
		String rawPassword=userDto.getPassword();
		String encryptedPassword = passwordEncoder.encode(rawPassword);
		userDto.setPassword(encryptedPassword);
		
		//userDto를 user 엔티티로 변환
		User user = userDto.toEntity();
		userRepository.save(user);
		
	}
	
	public Optional<UserDto> searchUser(Long id) {
		return userRepository.findById(id)
				.map(UserDto::fromEntity);
	}
	public Optional<User> findByUid(String uid) {
	    return userRepository.findByUid(uid);
	}
	
	
	
	// 모든유저조회 
	public List<UserDto> getAllUsers(){
		List<User> users = userRepository.findAll();
		//여기서 getInstace를 사용하여 roleType을 반환
		return users.stream()
				.map(UserDto::fromEntity) 
				.collect(Collectors.toList());
	}

}
