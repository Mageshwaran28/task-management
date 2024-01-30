package com.zerp.taskmanagement.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerp.taskmanagement.dbentity.Role;

public interface RoleRepository extends JpaRepository<Role , Long>{

    Role findByRole(String role);

    boolean existsByRole(String newRole);
    
}
