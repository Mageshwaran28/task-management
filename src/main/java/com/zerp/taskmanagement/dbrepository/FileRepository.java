package com.zerp.taskmanagement.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerp.taskmanagement.dbentity.File;

public interface FileRepository extends JpaRepository<File , Long>{
    
}
