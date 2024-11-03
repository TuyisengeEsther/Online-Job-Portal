package com.example.online_job_protal.repository;

import com.example.online_job_protal.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
//    List<Role> findByName(String roleName);
}
