package com.zerp.taskmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerp.taskmanagement.model.TaskAssignment;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment,Long>{

    boolean existsByTaskIdAndAssigneeId(long id, long assignee);

    List<TaskAssignment> findByAssigneeId(long id);

    void deleteByTaskIdAndAssigneeId(Long id, long id2);
    
}
