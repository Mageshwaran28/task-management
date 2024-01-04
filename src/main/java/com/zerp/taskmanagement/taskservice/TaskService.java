package com.zerp.taskmanagement.taskservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.dbentity.Task;
import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dbrepository.ProjectRepository;
import com.zerp.taskmanagement.dbrepository.TaskRepository;
import com.zerp.taskmanagement.dbrepository.UserRepository;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findByPriority(String priority) {
        return taskRepository.findByPriority(priority);
    }

    public String addTask(Task task) {
        taskRepository.save(task);
        return "success";
    }

    public Task findByTaskId(long taskId) {
        return taskRepository.findByTaskId(taskId);
    }

    public Task assignTask(long taskId, long creatorId, long assigneeId) {
        Task task = taskRepository.findById(taskId).get();
        User creator = userRepository.findById(creatorId).get();
        User assignee = userRepository.findById(assigneeId).get();
        task.setCreator(creator);
        task.setAssignee(assignee);
        return taskRepository.save(task);
    }

    public Task taskAssign(long taskId, long projectId, long creatorId, long assigneeId) {
        Task task = taskRepository.findById(taskId).get();
        User creator = userRepository.findById(creatorId).get();
        User assignee = userRepository.findById(assigneeId).get();
        Project project = projectRepository.findById(projectId).get();
        task.setProject(project);
        task.setCreator(creator);
        task.setAssignee(assignee);
        return taskRepository.save(task);
    }

    public List<Task> findByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

}
