package com.zerp.taskmanagement.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerp.taskmanagement.dbentity.ProjectAssignments;

@Repository
public interface ProjectAssignmentsRepository extends JpaRepository<ProjectAssignments , Long> {

    
} 