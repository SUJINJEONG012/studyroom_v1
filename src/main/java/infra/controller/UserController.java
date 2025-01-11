package infra.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import infra.entity.constant.UserRoleType;
import infra.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
public class UserController {

	private final UserService userService;
	
//	@GetMapping("/")
//	public String mainPage(Model model) {
//		
//		 // 현재 로그인한 사용자 정보 가져오기
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        // principal이 UserDetails 객체인 경우에 사용자 정보 가져오기
//        if (principal instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) principal;
//            String username = userDetails.getUsername(); // 사용자 이름
//            System.out.println("현재 로그인한 사용자 이름: " + username);
//            model.addAttribute("username", username);
//        } else {
//            // 인증되지 않은 경우
//            System.out.println("로그인된 사용자가 없습니다.");
//        }
//        
//        
//		// 모든 유저 정보를 가져옴 
//		List<UserDto> users = userService.getAllUsers();
//		System.out.println("users : "+ users); // DTO 리스트 확인
//		model.addAttribute("users", users);
//		return "index";
//	}
	
	@GetMapping("/")
	public String mainPage(Model model) {
	    // 현재 로그인한 사용자 정보 가져오기
	    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

	    if (principal instanceof UserDetails) {
	        UserDetails userDetails = (UserDetails) principal;
	        String username = userDetails.getUsername();
	        System.out.println("현재 로그인한 사용자 이름: " + username);
	        model.addAttribute("username", username);
	    } else {
	        // 인증되지 않은 경우
	        System.out.println("로그인된 사용자가 없습니다.");
	        model.addAttribute("username", "Guest"); // 기본 메시지 설정
	    }

	    // 모든 유저 정보를 가져옴
	    List<UserDto> users = userService.getAllUsers();
	    System.out.println("users : " + users); // DTO 리스트 확인
	    model.addAttribute("users", users);

	    return "index"; // index.html로 이동
	}
	
	
	
	
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error, Model model) {
		 log.info("로그인 페이지 !");
		if (error != null) {
	        model.addAttribute("errorMessage", "아이디 또는 비밀번호가 잘못되었습니다.");
	    }
	    return "login"; // 로그인 페이지로 이동
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody UserRequest userRequest) {
	   log.info("post 로그인진입!");
		try {
	        // UserRequest에서 UserDto로 변환
	        UserDto userDto = userRequest.toDto(UserRoleType.GUEST);

	        // 사용자 인증 및 JWT 토큰 발급
	        String token = userService.authenticateUser(userDto);  // JWT 토큰 발급

	        // 로그인 성공 시 메시지 및 토큰 반환
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "로그인성공 !");
	        response.put("token", token);

	        return ResponseEntity.ok(response); // 200 OK와 함께 응답
	    } catch (Exception e) {
	        // 로그인 실패 시 메시지 반환
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "로그인 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // 401 Unauthorized
	    }
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
