package infra.config.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import infra.entity.User;
import infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

	 private final UserRepository userRepository;
	
	 @Override
	 public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
	   
		 // Optional<User>에서 User를 안전하게 꺼내기
	        User user = userRepository.findByUid(uid)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

		 return new PrincipalDetails(user);
	    
	  
	 }


}
