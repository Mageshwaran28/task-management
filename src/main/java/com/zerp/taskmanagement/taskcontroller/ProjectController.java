package com.zerp.taskmanagement.taskcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.taskservice.ProjectService;

@RestController
@RequestMapping(value = "/taskmanagement")
public class ProjectController {
    
    @Autowired
    ProjectService projectService;

    @GetMapping(value = "/projects")
    public List<Project> getProjects(){
        return projectService.getProjects();
    }

}
