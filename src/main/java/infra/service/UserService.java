package infra.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
	
	
	public String authenticateUser(UserDto userDto) {
	    // UID로 사용자를 찾음
		 User user = userRepository.findByUid(userDto.getUid())
		            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	    
	    if (user == null) {
	        return "User not found";
	    }

	    // 비밀번호 검증
	    if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
	        return "Invalid credentials";
	    }

	    // JWT 토큰 생성 등 추가 작업
	    return "User logged in successfully!";
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
