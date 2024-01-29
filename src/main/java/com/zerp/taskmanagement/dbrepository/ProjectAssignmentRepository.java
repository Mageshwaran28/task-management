package com.zerp.taskmanagement.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerp.taskmanagement.dbentity.ProjectAssignment;

@Repository
public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment , Long> {

    
} 