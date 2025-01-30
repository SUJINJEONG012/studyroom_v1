package infra.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import infra.config.auth.PrincipalDetails;

@RestController
@RequestMapping("/api")
public class UserApiController {

	@GetMapping("/user")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        Map<String, String> response = new HashMap<>();
        response.put("username", userDetails.getUsername());

        return ResponseEntity.ok(response);
    }
}
