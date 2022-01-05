package com.inflearn.springsecurityiml.application;


import com.inflearn.springsecurityiml.application.service.RolesService;
import com.inflearn.springsecurityiml.application.service.UserService;
import com.inflearn.springsecurityiml.application.service.dto.UserDto;
import com.inflearn.springsecurityiml.domain.Account;
import com.inflearn.springsecurityiml.domain.AccountDto;
import com.inflearn.springsecurityiml.domain.Roles;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserManagerController {

    private final UserService userService;

    private final RolesService roleService;

    @GetMapping(value = "/admin/accounts")
    public String getUsers(Model model) {
        List<Account> accounts = userService.getUsers();
        model.addAttribute("accounts", accounts);
        return "admin/user/list";
    }

    @PostMapping(value = "/admin/accounts")
    public String createUser(AccountDto accountDto) {
        userService.createUser(accountDto);

        return "redirect:/admin/user/list";
    }

    @GetMapping(value = "/admin/accounts/{id}")
    public String getUser(@PathVariable(value = "id") Long id, Model model) {
        UserDto userDto = userService.getUser(id);
        List<Roles> roleList = roleService.getRoles();

        model.addAttribute("act", (id > 0) ? "modify" : "add");
        model.addAttribute("account", userDto);
        model.addAttribute("roleList", roleList);

        return "admin/user/detail";
    }

}
