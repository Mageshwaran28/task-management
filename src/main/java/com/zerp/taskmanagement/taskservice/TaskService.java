package com.zerp.taskmanagement.taskservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.dbentity.Task;
import com.zerp.taskmanagement.dbrepository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

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
    
}
