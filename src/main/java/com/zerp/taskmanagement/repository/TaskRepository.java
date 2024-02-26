package com.zerp.taskmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerp.taskmanagement.model.Task;
import com.zerp.taskmanagement.enums.Priority;
import com.zerp.taskmanagement.enums.Status;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findDepthById(long parentTaskId);

    List<Task> findByParentTaskIdNull();

    List<Task> findByPriority(Priority priority);

    List<Task> findByStatus(Status status);

    List<Task> findByCreatorId(long id);

    List<Task> findByDueDateBefore(LocalDateTime now);

    boolean existsByIdAndCreatorId(Long id, long userId);

}
