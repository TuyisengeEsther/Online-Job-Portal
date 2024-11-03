package com.example.online_job_protal.Service;

import com.example.online_job_protal.Model.Role;
import com.example.online_job_protal.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public void addAllowedMenu(Long roleId, String menu) {
        Role role = findById(roleId);
        if (role != null && !role.getAllowedMenus().contains(menu)) {
            role.getAllowedMenus().add(menu);
            save(role);
        }
    }


    public void removeAllowedMenu(Long roleId, String menu) {
        Role role = findById(roleId);
        if (role != null) {
            role.getAllowedMenus().remove(menu);
            save(role);
        }
    }

    public List<String> getAllowedMenus(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role != null) {
            return role.getAllowedMenus(); // Directly return the List<String>
        }
        return Collections.emptyList(); // Return an empty list if no role found
    }

}
