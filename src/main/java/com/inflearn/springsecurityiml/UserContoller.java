package com.inflearn.springsecurityiml;

import com.inflearn.springsecurityiml.domain.AccountDto;
import com.inflearn.springsecurityiml.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping(value = "/login", method = {RequestMethod.GET,RequestMethod.POST})
    public String login(
        @RequestParam(required = false) String error,
        @RequestParam(required = false) String errorMessage, Model model) {

        model.addAttribute("error", error);
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }

    @PostMapping("/users")
    public String createUser(AccountDto accountDto) {
        userService.createUser(accountDto);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }

}
