package com.zerp.taskmanagement.taskcontroller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@RestController
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("/tasks")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Task createTask(@RequestBody TaskDTO taskDTO) {
        return taskService.createTask(taskDTO);
    }

    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return taskService.getTasks();
    }

    @PostMapping("/tasks/{id}/upload")
    public File uploadFile(@PathVariable() Long id, @RequestParam("file") MultipartFile uploadFile)
            throws IOException {
        return taskService.uploadFile(id, uploadFile);
    }

    @GetMapping("files/{id}")
    public ResponseEntity<?> getFile(@PathVariable("id") Long id) throws IOException {

        File receivedFile = taskService.getFile(id);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(receivedFile.getType()))
                .body(receivedFile.getDocument());
    }

    @PostMapping("/tasks/{id}/assignees")
    public String createAssignee(@PathVariable long id, @RequestBody Set<String> emails) {
        return taskService.createAssignee(id, emails);
    }

    @GetMapping("/tasks/{id}")
    public Task geTask(@PathVariable Long id) {
        return taskService.geTask(id);
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

    @GetMapping("tasks/creators/{email}")
    public List<Task> getTasksByCreatorId(@PathVariable String email) {
        return taskService.getTasksByCreatorId(email);
    }

    @GetMapping("tasks/assignees/{email}")
    public List<Task> getTasksByAssigneeId(@PathVariable String email) {
        return taskService.getTasksByAssigneeId(email);
    }

    @GetMapping("tasks/due")
    public List<Task> getTasksByDueDate() {
        return taskService.getTasksByDueDate();
    }

    @DeleteMapping("tasks/{id}")
    public String deleteTaskById(@PathVariable Long id) {
        return taskService.deleteTaskById(id);
    }

    @DeleteMapping("files/{id}")
    public String deleteTaskFileById(@PathVariable Long id) {
        return taskService.deleteTaskFileById(id);
    }

    @DeleteMapping("tasks/{id}/assignees/{email}")
    public String deleteTaskAssigneesById(@PathVariable Long id, @PathVariable String email) {
        return taskService.deleteTaskAssigneesById(id, email);
    }

    @PutMapping("/tasks/{id}/status/{statusId}")
    public String updateTaskStatus(@PathVariable Long id, @PathVariable String statusId) {
        Status status = Status.fromString(statusId);
        return taskService.updateTaskStatus(id, status);
    }

    @PutMapping("tasks/{id}")
    public String updatetask(@PathVariable Long id, @RequestBody TaskUpdateDTO taskUpdateDTO) {
        return taskService.updateTask(id, taskUpdateDTO);
    }

}
