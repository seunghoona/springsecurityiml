package com.inflearn.springsecurityiml.application;

import com.inflearn.springsecurityiml.domain.Resources;
import com.inflearn.springsecurityiml.domain.Roles;
import com.inflearn.springsecurityiml.domain.RolesRepo;
import com.inflearn.springsecurityiml.application.service.ResourceService;
import com.inflearn.springsecurityiml.application.service.RolesService;
import com.inflearn.springsecurityiml.application.service.dto.ResourcesDto;
import com.inflearn.springsecurityiml.factory.SecurityResourceService;
import com.inflearn.springsecurityiml.metadatasource.UrlFilterInvocationSecurityMetaDatasource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ResourceController {

    private ResourceService resourcesService;

    private RolesRepo roleRepository;

    private RolesService roleService;

    private UrlFilterInvocationSecurityMetaDatasource urlFilterInvocationSecurityMetaDatasource;


    @GetMapping(value="/admin/resources")
    public String getResources(Model model) throws Exception {

        List<Resources> resources = resourcesService.selectResources();
        model.addAttribute("resources", resources);

        return "admin/resource/list";
    }

    @PostMapping(value="/admin/resources")
    public String createResources(ResourcesDto resourcesDto) throws Exception {

        ModelMapper modelMapper = new ModelMapper();
        Roles role = roleRepository.findByRoleName(resourcesDto.getRoleName());
        Set<Roles> roles = new HashSet<>();
        roles.add(role);
        Resources resources = modelMapper.map(resourcesDto, Resources.class);
        resources.setRoleSet(roles);

        resourcesService.insertResources(resources);

        return "redirect:/admin/resources";
    }

    @GetMapping(value="/admin/resources/register")
    public String viewRoles(Model model) throws Exception {

        List<Roles> roleList = roleService.getRoles();
        urlFilterInvocationSecurityMetaDatasource.reload();

        model.addAttribute("roleList", roleList);
        model.addAttribute("resources", new Resources());

        return "admin/resource/detail";
    }

    @GetMapping(value="/admin/resources/{id}")
    public String getResources(@PathVariable String id, Model model) throws Exception {

        List<Roles> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);

        Resources resources = resourcesService.selectResources(Long.valueOf(id));
        model.addAttribute("resources", resources);

        return "admin/resource/detail";
    }

    @GetMapping(value="/admin/resources/delete/{id}")
    public String removeResources(@PathVariable String id, Model model) throws Exception {

        resourcesService.selectResources(Long.valueOf(id));
        resourcesService.deleteResources(Long.valueOf(id));
        urlFilterInvocationSecurityMetaDatasource.reload();

        return "redirect:/admin/resources";
    }

}
