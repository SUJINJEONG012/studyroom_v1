package infra.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import infra.dto.UserDto;
import infra.dto.request.UserRequest;
import infra.entity.User;
import infra.entity.constant.UserRoleType;
import infra.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserService userService;
	
	@GetMapping("/")
	public String mainPage(Model model) {
		
		 // 현재 로그인한 사용자 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // principal이 UserDetails 객체인 경우에 사용자 정보 가져오기
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername(); // 사용자 이름
            System.out.println("현재 로그인한 사용자 이름: " + username);
            model.addAttribute("username", username);
        } else {
            // 인증되지 않은 경우
            System.out.println("로그인된 사용자가 없습니다.");
        }
        
        
		// 모든 유저 정보를 가져옴 
		List<UserDto> users = userService.getAllUsers();
		System.out.println("users : "+ users); // DTO 리스트 확인
		model.addAttribute("users", users);
		return "index";
	}
	
	
	
	
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error, Model model) {
	    if (error != null) {
	        model.addAttribute("errorMessage", "아이디 또는 비밀번호가 잘못되었습니다.");
	    }
	    return "login"; // 로그인 페이지로 이동
	}
	
	
	
	 @PostMapping("/login")
	    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
	        // UserService에서 인증 로직 호출
	        String responseMessage = userService.authenticateUser(userDto);
	        
	        // 성공 메시지
	        if (responseMessage.equals("User logged in successfully!")) {
	            return ResponseEntity.ok(responseMessage);
	        }
	        
	        // 인증 실패 메시지
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessage);
	    }
	

	@GetMapping("/signup")
	public String guestSignupPage() {
		return "guest-sign-up";
	}
	
	
	
	@PostMapping("/signup")
	public String signup(@ModelAttribute UserRequest userRequest, Model model) {
		try {
			UserDto userDto = userRequest.toDto(UserRoleType.GUEST);
			userService.registerUser(userDto);
			System.out.println("회원가입에서 입력한 값 : " + userRequest); // 로그 추가
			System.out.println("디비에저장되는값 : " + userDto); // 로그 추가
			
			return "redirect:/login";
		}catch(Exception e) {
			System.out.println("회원가입 오류: " + 	e.getMessage()); 
			 model.addAttribute("errorMessage", "회원가입에 실패했습니다: " + e.getMessage());
		        return "guest-sign-up"; // 오류 발생 시 회원가입 페이지로 복귀
		}
	}
	
	
	
	
}
