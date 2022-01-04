package com.inflearn.springsecurityiml;

import com.inflearn.springsecurityiml.domain.Roles;
import com.inflearn.springsecurityiml.service.RolesService;
import com.inflearn.springsecurityiml.service.dto.RoleDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RolesController {


    private final RolesService roleService;

    @GetMapping(value = "/admin/roles")
    public String getRoles(Model model) {
        List<Roles> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        return "admin/role/list";
    }

    @GetMapping(value = "/admin/roles/register")
    public String viewRoles(Model model) {
        Roles role = new Roles();
        model.addAttribute("role", role);
        return "admin/role/detail";
    }

    @PostMapping(value = "/admin/roles")
    public String createRole(RoleDto roleDto) {

        ModelMapper modelMapper = new ModelMapper();

        Roles role = new Roles(roleDto.getRoleName(), roleDto.getRoleDesc());

        roleService.createRole(role);

        return "redirect:/admin/roles";
    }

    @GetMapping(value = "/admin/roles/{id}")
    public String getRole(@PathVariable String id, Model model) {
        Roles role = roleService.getRole(Long.parseLong(id));
        model.addAttribute("role", role);
        return "admin/role/detail";
    }
}
