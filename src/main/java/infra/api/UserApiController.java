package infra.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import infra.config.auth.PrincipalDetails;
import infra.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserApiController {

	@Value("${jwt.secretKey}")  // application.properties에서 SECRET_KEY 가져오기
    private String secretKey;
	
	private final UserService userService;
	
	
	 
	@GetMapping("/user")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        Map<String, String> response = new HashMap<>();
        response.put("uid", userDetails.getUsername());

        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/mypage")
	public ResponseEntity<?> getUserDetail(HttpServletRequest request, Authentication authentication) {
	    String authHeader = request.getHeader("Authorization");
	    System.out.println("📌 Authorization 헤더: " + authHeader); // 콘솔에서 확인

	    if (authentication == null) {
	        System.out.println("🚨 인증 실패: authentication 객체가 null");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
	    }

	    PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
	    System.out.println("📌 인증된 사용자 UID: " + userDetails.getUsername()); // 확인용

	    Map<String, String> response = new HashMap<>();
	    response.put("uid", userDetails.getUsername());
	    response.put("email", userDetails.getUserDto().getEmail());

	    return ResponseEntity.ok(response);
	}
	
}
