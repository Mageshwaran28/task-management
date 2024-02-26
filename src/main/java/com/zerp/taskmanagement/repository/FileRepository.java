package com.zerp.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerp.taskmanagement.model.File;

public interface FileRepository extends JpaRepository<File , Long>{
    
}
