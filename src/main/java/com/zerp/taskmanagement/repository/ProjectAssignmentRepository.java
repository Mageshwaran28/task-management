package com.zerp.taskmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerp.taskmanagement.model.ProjectAssignment;

@Repository
public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment , Long> {

    boolean existsByProjectIdAndAssigneeId(long id, long assignee);

    List<ProjectAssignment> findByAssigneeId(long id);

    void removeByProjectIdAndAssigneeId(long id, long id2);

} 