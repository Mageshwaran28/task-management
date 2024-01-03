package com.zerp.taskmanagement.taskcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.dbentity.Task;
import com.zerp.taskmanagement.taskservice.TaskService;

@RestController
@RequestMapping("/taskmanagement/tasks")
public class TaskController {
    
    @Autowired
    TaskService taskService;

    @GetMapping("/all")
    public List<Task> getAllTasks(){
        return taskService.findAll();
    }

    @GetMapping("/priority/{priority}")
    public List<Task> getTasksByPriority(@PathVariable String priority){
        return taskService.findByPriority(priority);
    }

    @PostMapping("/add")
    public String addTask(@RequestBody Task task ){
        return taskService.addTask(task);
    }

}
