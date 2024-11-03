package com.example.online_job_protal.Controller;
import com.example.online_job_protal.Model.Role;
import com.example.online_job_protal.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public String createRole(Model model, @RequestParam String name, @RequestParam(required = false) String allowedMenus) {
        List<String> menus = allowedMenus != null ? Arrays.asList(allowedMenus.split(",")) : null;
        Role newRole = new Role(name, menus);
        roleService.save(newRole);
        model.addAttribute("errorMessage", "Role Added");

        return "add-role"; // Redirect to admin dashboard
    }

    @GetMapping("/add")
    public String showAddRolePage() {
        return "add-role";
    }
    @GetMapping
    public String listRoles(Model model) {
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        return "role-list"; // The view name for displaying the list of roles
    }
}

