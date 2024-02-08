package com.zerp.taskmanagement.taskcontroller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zerp.taskmanagement.dbentity.File;
import com.zerp.taskmanagement.dbentity.Task;
import com.zerp.taskmanagement.dto.TaskDTO;
import com.zerp.taskmanagement.dto.TaskUpdateDTO;
import com.zerp.taskmanagement.myenum.Priority;
import com.zerp.taskmanagement.myenum.Status;
import com.zerp.taskmanagement.taskservice.TaskService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("/tasks")
    public Task createTask(@RequestBody TaskDTO taskDTO, HttpServletRequest request) {
        return taskService.createTask(taskDTO, request);
    }

    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return taskService.getTasks();
    }

    @PostMapping("/tasks/{id}/upload")
    public File uploadFile(@PathVariable() Long id, @RequestParam("file") MultipartFile uploadFile, HttpServletRequest request)
            throws IOException {
        return taskService.uploadFile(id, uploadFile, request);
    }

    @GetMapping("files/{id}")
    public ResponseEntity<?> getFile(@PathVariable("id") Long id , HttpServletRequest request) throws IOException {

        File receivedFile = taskService.getFile(id, request);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(receivedFile.getType()))
                .body(receivedFile.getDocument());
    }

    @PostMapping("/tasks/{id}/assignees")
    public String createAssignee(@PathVariable long id, @RequestBody Set<Long> assigneesId, HttpServletRequest request) {
        return taskService.createAssignee(id, assigneesId, request);
    }

    @GetMapping("/tasks/{id}")
    public Task geTask(@PathVariable Long id, HttpServletRequest request) {
        return taskService.geTask(id, request);
    }

    @GetMapping("/tasks/priority/{id}")
    public List<Task> getTasksByPriority(@PathVariable String id) {
        Priority priority = Priority.fromString(id);
        return taskService.getTasksByPriority(priority);
    }

    @GetMapping("/tasks/status/{id}")
    public List<Task> getTasksByStatus(@PathVariable String id) {
        Status status = Status.fromString(id);
        return taskService.getTasksByStatus(status);
    }

    @GetMapping("tasks/creators")
    public List<Task> getTasksByCreatorId(HttpServletRequest request) {
        return taskService.getTasksByCreatorId(request);
    }

    @GetMapping("tasks/assignees")
    public List<Task> getTasksByAssigneeId(HttpServletRequest request) {
        return taskService.getTasksByAssigneeId(request);
    }

    @GetMapping("tasks/due")
    public List<Task> getTasksByDueDate() {
        return taskService.getTasksByDueDate();
    }

    @DeleteMapping("tasks/{id}")
    public String deleteTaskById(@PathVariable Long id, HttpServletRequest request) {
        return taskService.deleteTaskById(id, request);
    }

    @DeleteMapping("files/{id}")
    public String deleteTaskFileById(@PathVariable Long id, HttpServletRequest request) {
        return taskService.deleteTaskFileById(id, request);
    }

    @DeleteMapping("tasks/{id}/assignees/{assigneeId}")
    public String deleteTaskAssigneesById(@PathVariable Long id, @PathVariable Long assigneeId, HttpServletRequest request) {
        return taskService.deleteTaskAssigneesById(id, assigneeId, request);
    }

    @PutMapping("/tasks/{id}/status/{statusId}")
    public String updateTaskStatus(@PathVariable Long id, @PathVariable String statusId, HttpServletRequest request)  {
        Status status = Status.fromString(statusId);
        return taskService.updateTaskStatus(id, status, request);
    }

    @PutMapping("tasks/{id}")
    public String updatetask(@PathVariable Long id, @RequestBody TaskUpdateDTO taskUpdateDTO, HttpServletRequest request) {
        return taskService.updateTask(id, taskUpdateDTO, request);
    }

}
