package com.zerp.taskmanagement.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerp.taskmanagement.dbentity.ProjectAssignment;

public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment , Long> {

    
}