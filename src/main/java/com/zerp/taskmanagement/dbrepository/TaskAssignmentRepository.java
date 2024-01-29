package com.zerp.taskmanagement.dbrepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zerp.taskmanagement.dbentity.TaskAssignment;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment,Long>{

    boolean existsByTaskIdAndAssigneeId(long id, long assignee);

    List<TaskAssignment> findByAssigneeId(long id);

    void deleteByTaskIdAndAssigneeId(Long id, long id2);

    @Query("delete from TaskAssignment where task_id = :id and assignee_id = :id2")
    void removeByTaskIdAndAssigneeId(Long id, long id2);

    TaskAssignment findByTaskIdAndAssigneeId(Long id, long id2);
    
}
