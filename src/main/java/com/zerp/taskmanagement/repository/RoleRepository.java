package com.zerp.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerp.taskmanagement.model.Role;

public interface RoleRepository extends JpaRepository<Role , Long>{

    Role findByRole(String role);

    boolean existsByRole(String newRole);
    
}
