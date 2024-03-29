package com.zerp.taskmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerp.taskmanagement.model.User;

@Repository
public interface UserRepository extends JpaRepository <User , Long>{

    boolean existsByEmail(String email);

    User findByEmailIgnoreCase(String creator);

    boolean existsByEmailIgnoreCase(String email);

    User findByEmailAndPassword(String email, String currentPassword);

    Optional<User> findById(Integer id);

    User findByIdAndPassword(Long id, String currentPassword);

}
