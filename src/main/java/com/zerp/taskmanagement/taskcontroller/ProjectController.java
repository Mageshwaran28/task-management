package com.zerp.taskmanagement.taskcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.taskservice.ProjectService;

@RestController
@RequestMapping(value = "/taskmanagement/projects")
public class ProjectController {
    
    @Autowired
    ProjectService projectService;

    @GetMapping(value = "/all")
    public ResponseEntity<List<Project>> getProjects(){
        return projectService.getProjects();
    }

     @PostMapping("/add")
    public ResponseEntity<String> addProject(@RequestBody Project project ){
        return projectService.addProject(project);
    }

}
