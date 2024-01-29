package com.zerp.taskmanagement.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerp.taskmanagement.dbentity.User;

@Repository
public interface UserRepository extends JpaRepository <User , Long>{

    boolean existsByEmail(String email);

    User findByEmailIgnoreCase(String creator);

    boolean existsByEmailIgnoreCase(String email);

    User findByEmailAndPassword(String email, String currentPassword);

}
