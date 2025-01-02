package infra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import infra.dto.UserDto;
import infra.dto.request.UserRequest;
import infra.entity.constant.UserRoleType;
import infra.service.UserService;
import lombok.RequiredArgsConstructor;

/**
 * API
 * - 회원가입
 * - 로그인
 * 
 */
@RequiredArgsConstructor
@Controller
public class MainController {

	private final UserService userService;

//	@GetMapping("/")
//	public String mainPage() {
//		return "forward:/posts";
//	}
	

	
	@GetMapping("/host/signup")
	public String hostSignupPage() {
		return "host-sign-up";
	}

	

}
