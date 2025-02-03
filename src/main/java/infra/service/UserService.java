package infra.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import infra.config.jwt.JwtProperties;
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
	
	
    
    
	// 사용자 인증 후 JWT 토큰 발급
    public String authenticateUser(UserDto userDto) throws Exception {
        // 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken = 
            new UsernamePasswordAuthenticationToken(userDto.getUid(), userDto.getPassword());

        // 인증 처리
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 인증 성공 후 JWT 토큰 생성
        String jwtToken = JWT.create()
            .withSubject(authentication.getName())  // 사용자 이름
            .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))  // 만료 시간
            .withClaim("uid", userDto.getUid())  // 추가적인 claim, 예: 사용자 ID
            .sign(Algorithm.HMAC512(JwtProperties.SECRET));  // 비밀 키로 서명

        return jwtToken;
    }
    

	public void registerUser(UserDto userDto) {
		
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
	
	
	// 호스트모드 
//	public void updateHostMode(Long id, boolean hostMode) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!"ROLE_HOST".equals(user.getRoleType())) {
//            throw new RuntimeException("Only ROLE_HOST users can toggle host mode");
//        }
//
//        // DTO변환후 값 변경
//        UserDto userDto = UserDto.fromEntity(user);
//        userDto.setHostMode(hostMode);
//        
//        // 다시 엔티티로 변환 후 저장
//        User updateUser = userDto.toEntity();
//        userRepository.save(updateUser);
//    }
	
	
	
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


	public User getUserById(String userId) {
		// TODO Auto-generated method stub
		return null;
	}



    

}
