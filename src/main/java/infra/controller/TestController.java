package infra.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/test-header")
	public String testHeader(@RequestHeader(value= "Authorization", required = false)String authorization) {
		System.out.println("Authorization Header : " + authorization);
		return authorization != null ? "Header found: " + authorization : "Authorization header is missing!";
	}
}
