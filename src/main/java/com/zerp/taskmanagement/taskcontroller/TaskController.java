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
import com.zerp.taskmanagement.dto.TaskDTO;
import com.zerp.taskmanagement.myenum.Priority;
import com.zerp.taskmanagement.myenum.Status;
import com.zerp.taskmanagement.taskservice.TaskService;

@RestController
@RequestMapping("/taskmanagement/")
public class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    @GetMapping("/tasks/priority/{priorityId}")
    public List<Task> getTasksByPriority(@PathVariable String priorityId) {
        Priority priority = Priority.fromString(priorityId);
        return taskService.findByPriority(priority);
    }

    @GetMapping("/tasks/status/{statusId}")
    public List<Task> getTasksByStatus(@PathVariable String statusId) {
        Status status = Status.fromString(statusId);
        return taskService.findByStatus(status);
    }

    @GetMapping("/task/{taskId}")
    public Task getTaskById(@PathVariable long taskId) {
        return taskService.findByTaskId(taskId);
    }

    @PostMapping("/task/add")
    public String addTask(@RequestBody TaskDTO taskDTO) {
        return taskService.addTask(taskDTO);
    }

}
