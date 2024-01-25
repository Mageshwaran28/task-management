package com.zerp.taskmanagement.taskcontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.zerp.taskmanagement.dbentity.File;
import com.zerp.taskmanagement.dbentity.Task;
import com.zerp.taskmanagement.dto.TaskDTO;
import com.zerp.taskmanagement.jsonview.View;
import com.zerp.taskmanagement.taskservice.TaskService;

@RestController
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("/tasks")
    public Task createTask(@RequestBody TaskDTO taskDTO) {
        return taskService.createTask(taskDTO);
    }

    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return taskService.getTasks();
    }

    @PostMapping("/tasks/{id}/upload")
    public File uploadFile(@PathVariable() String id, @RequestParam("file") MultipartFile uploadFile)
            throws IOException {
        Long taskId = Long.parseLong(id);
        return taskService.uploadFile(taskId, uploadFile);
    }

    @GetMapping("files/{id}")
    public ResponseEntity<?> getFile(@PathVariable("id") Long id) throws IOException {

        File receivedFile = taskService.getFile(id);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(receivedFile.getType()))
                .body(receivedFile.getDocument());
    }

}
