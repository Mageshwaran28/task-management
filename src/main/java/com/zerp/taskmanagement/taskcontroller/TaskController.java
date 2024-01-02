package com.zerp.taskmanagement.taskcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.dbentity.Task;
import com.zerp.taskmanagement.taskservice.TaskService;

@RestController
@RequestMapping(value ="/taskmanagement")
public class TaskController {
    
    @Autowired
    TaskService taskService;

    @GetMapping("/tasks")
    public List<Task> getAllTasks(){
        return taskService.findAll();
    }

}
