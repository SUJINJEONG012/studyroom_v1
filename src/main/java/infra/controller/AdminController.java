package infra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import infra.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
@Slf4j
public class AdminController {

	private final AdminService adminService;
	
	@GetMapping("")
	public String adminIndext() {
		return "/admin/index";
	}
	
	
}
