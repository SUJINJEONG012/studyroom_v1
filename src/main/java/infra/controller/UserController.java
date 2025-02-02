package infra.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import infra.dto.UserDto;
import infra.dto.request.UserRequest;
import infra.entity.constant.UserRoleType;
import infra.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

	
	private final UserService userService;
	
	
	@GetMapping("/mypage")
	public String myPage() {
		return "/user/mypage";
	}
	
	 
	
	// 모든 사용자 가져오기
    @GetMapping("/users")
    @ResponseBody
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
    


	@GetMapping("/login")
	public String login(@RequestParam(required = false) String error, Model model) {
		 log.info("로그인 페이지 !");
		if (error != null) {
	        model.addAttribute("errorMessage", "아이디 또는 비밀번호가 잘못되었습니다.");
	    }
	    return "login"; // 로그인 페이지로 이동
	}
	

	@PostMapping("/login")
	@ResponseBody
	public ResponseEntity<Map<String, String>> login(@RequestBody UserRequest userRequest) {
	    log.info("로그인 시도: {}", userRequest.getUid());

	    try {
	        // UserRequest에서 UserDto로 변환
	        UserDto userDto = userRequest.toDto(UserRoleType.GUEST);

	        // 사용자 인증 및 JWT 토큰 발급
	        String token = userService.authenticateUser(userDto);  // JWT 토큰 발급

	        // 로그인 성공 시 메시지 및 토큰 반환
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "로그인 성공!");
	        response.put("token", token);  // 토큰을 응답 본문에 포함시킴

	        // 200 OK 응답, 헤더에 Authorization 포함
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "Bearer " + token);  // 헤더에 토큰 추가

	        return ResponseEntity.ok()
	                             .headers(headers)
	                             .body(response);  // 응답 본문과 헤더 모두 반환

	    } catch (Exception e) {
	        // 로그인 실패 시 메시지 반환
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "로그인 실패: " + e.getMessage());
	        
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)  // 401 Unauthorized
	                             .body(response);
	    }
	}


	@GetMapping("/signup")
	public String guestSignupPage() {
		return "/user/guest-sign-up";
	}
	
	
	
	@PostMapping("/signup")
	public String signup(@ModelAttribute UserRequest userRequest, Model model) {
		try {
			UserDto userDto = userRequest.toDto(UserRoleType.GUEST);
			userService.registerUser(userDto);
			System.out.println("회원가입에서 입력한 값 : " + userRequest); // 로그 추가
			System.out.println("디비에저장되는값 : " + userDto); // 로그 추가
			
			return "redirect:/user/login";
		}catch(Exception e) {
			System.out.println("회원가입 오류: " + 	e.getMessage()); 
			 model.addAttribute("errorMessage", "회원가입에 실패했습니다: " + e.getMessage());
		        return "/user/guest-sign-up"; // 오류 발생 시 회원가입 페이지로 복귀
		}
	}
	
	
	
	
}
