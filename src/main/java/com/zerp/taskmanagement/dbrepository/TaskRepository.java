package com.zerp.taskmanagement.dbrepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerp.taskmanagement.dbentity.Task;
import com.zerp.taskmanagement.myenum.Priority;
import com.zerp.taskmanagement.myenum.Status;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findDepthById(long parentTaskId);

    List<Task> findByParentTaskIdNull();

    List<Task> findByPriority(Priority priority);

    List<Task> findByStatus(Status status);

    List<Task> findByCreatorId(long id);

    List<Task> findByDueDateBefore(LocalDateTime now);

}
