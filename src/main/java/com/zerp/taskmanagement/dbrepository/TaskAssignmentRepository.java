package com.zerp.taskmanagement.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerp.taskmanagement.dbentity.TaskAssignment;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment , Long>{
    
}
