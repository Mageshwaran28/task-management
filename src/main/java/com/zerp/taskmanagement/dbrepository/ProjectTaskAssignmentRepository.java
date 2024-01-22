package com.zerp.taskmanagement.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerp.taskmanagement.dbentity.ProjectTaskAssignment;

public interface ProjectTaskAssignmentRepository extends JpaRepository<ProjectTaskAssignment , Long>{
    
}
