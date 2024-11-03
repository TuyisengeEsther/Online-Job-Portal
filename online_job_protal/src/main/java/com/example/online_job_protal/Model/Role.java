package com.example.online_job_protal.Model;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    private List<String> allowedMenus; // New field for allowed menus

    // Constructors
    public Role() {}

    public Role(String name, List<String> allowedMenus) {
        this.name = name;
        this.allowedMenus = allowedMenus;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAllowedMenus() {
        return allowedMenus;
    }

    public void setAllowedMenus(List<String> allowedMenus) {
        this.allowedMenus = allowedMenus;
    }
}
