package com.zerp.taskmanagement.dbrepository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerp.taskmanagement.dbentity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task , Long> {

    Task findDepthById(long parentTaskId);

    List<Task> findByParentTaskIdNull();

    // List<Task> findByPriority(Priority priority);

    // Task findByTaskId(long taskId);

    // List<Task> findByStatus(Status status);

    // List<Task> findByDueDateBefore(Date date);

}
