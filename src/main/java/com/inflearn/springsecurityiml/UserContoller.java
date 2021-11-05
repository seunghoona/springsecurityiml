package com.inflearn.springsecurityiml;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.inflearn.springsecurityiml.domain.Account;
import com.inflearn.springsecurityiml.domain.AccountDto;
import com.inflearn.springsecurityiml.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserContoller {

	private final UserService userService;

	@GetMapping("/mypage")
	public String myPage() {
		return "user/mypage";
	}

	@GetMapping("/users")
	public String createUser() {
		return "user/login/register";
	}

	@PostMapping("/users")
	public String createUser(AccountDto accountDto) {
		userService.createUser(accountDto);
		return "redirect:/";
	}
}
