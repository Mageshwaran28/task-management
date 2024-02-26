package com.zerp.taskmanagement.controller;

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

import com.zerp.taskmanagement.dto.TaskDTO;
import com.zerp.taskmanagement.dto.TaskUpdateDTO;
import com.zerp.taskmanagement.model.File;
import com.zerp.taskmanagement.model.Task;
import com.zerp.taskmanagement.repository.FileRepository;
import com.zerp.taskmanagement.enums.Priority;
import com.zerp.taskmanagement.enums.Status;
import com.zerp.taskmanagement.exceptions.InvalidInputException;
import com.zerp.taskmanagement.exceptions.UnAuthorizeException;
import com.zerp.taskmanagement.service.TaskService;
import com.zerp.taskmanagement.utils.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TaskController extends CommonUtils {

    @Autowired
    TaskService taskService;

    @Autowired
    FileRepository fileRepository;

    @PostMapping("/tasks")
    public Task createTask(@RequestBody TaskDTO taskDTO, HttpServletRequest request) {
        return taskService.createTask(taskDTO, getUserEmail(request));
    }

    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return taskService.getTasks();
    }

    @PostMapping("/tasks/{id}/upload")
    public File uploadFile(@PathVariable() Long id, @RequestParam("file") MultipartFile uploadFile,
            HttpServletRequest request)
            throws IOException {
        String loginUser = getUserEmail(request);
        if (isAdminOrTaskCreator(id, loginUser) || isValidTaskAssignee(id, loginUser)) {
            return taskService.uploadFile(id, uploadFile, loginUser);
        }
        throw new UnAuthorizeException("Don't have permission to upload file");
    }

    @GetMapping("files/{id}")
    public ResponseEntity<?> getFile(@PathVariable("id") Long id, HttpServletRequest request) throws IOException {
        File file = fileRepository.findById(id).get();
        if (file == null) {
            throw new InvalidInputException("File not found: " + id);
        }
        Task task = file.getTask();
        String loginUser = getUserEmail(request);
        if (isAdminOrTaskCreator(task.getId(), loginUser) || isValidTaskAssignee(id, loginUser)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf(file.getType()))
                    .body(file.getDocument());
        }

        throw new UnAuthorizeException("Don't have permission to view this file " + id);
    }

    @PostMapping("/tasks/{id}/assignees")
    public String createAssignee(@PathVariable long id, @RequestBody Set<Long> assigneesId,
            HttpServletRequest request) {
        if (isAdminOrTaskCreator(id, getUserEmail(request))) {
            return taskService.createAssignee(id, assigneesId, request);
        }
        throw new UnAuthorizeException("Don't have permission to create assignees for this task." + id);

    }

    @GetMapping("/tasks/{id}")
    public Task geTask(@PathVariable Long id, HttpServletRequest request) {
        String loginUser = getUserEmail(request);
        if (isAdminOrTaskCreator(id, loginUser) || isValidTaskAssignee(id, loginUser)) {
            return taskService.geTask(id, request);
        }
        throw new UnAuthorizeException("Don't have permission to view this task" + id);

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
        return taskService.getTasksByCreator(getUserEmail(request));
    }

    @GetMapping("tasks/assignees")
    public List<Task> getTasksByAssigneeId(HttpServletRequest request) {
        return taskService.getTasksByAssignee(getUserEmail(request));
    }

    @GetMapping("tasks/due")
    public List<Task> getTasksByDueDate() {
        return taskService.getTasksByDueDate();
    }

    @DeleteMapping("tasks/{id}/assignees/{assigneeId}")
    public String deleteTaskAssigneesById(@PathVariable Long id, @PathVariable Long assigneeId,
            HttpServletRequest request) {
        if (isAdminOrTaskCreator(id, getUserEmail(request))) {
            return taskService.deleteTaskAssigneesById(id, assigneeId);
        }
        throw new UnAuthorizeException("Don't have permission to delete assignee from this task" + id);

    }

    @PutMapping("/tasks/{id}/status/{statusId}")
    public String updateTaskStatus(@PathVariable Long id, @PathVariable String statusId, HttpServletRequest request) {
        Status status = Status.fromString(statusId);
        String loginUser = getUserEmail(request);
        if (isAdminOrTaskCreator(id, loginUser) || isValidTaskAssignee(id, loginUser)) {
            return taskService.updateTaskStatus(id, status);
        }
        throw new UnAuthorizeException("Don't have permission to update status of this task." + id);

    }

    @PutMapping("tasks/{id}")
    public String updatetask(@PathVariable Long id, @RequestBody TaskUpdateDTO taskUpdateDTO,
            HttpServletRequest request) {
        if (isAdminOrTaskCreator(id, getUserEmail(request))) {
            return taskService.updateTask(id, taskUpdateDTO);
        }
        throw new InvalidInputException("Don't have permission to update this task. " + id);
    }

}
