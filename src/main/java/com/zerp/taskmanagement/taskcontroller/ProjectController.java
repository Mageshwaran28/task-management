package com.zerp.taskmanagement.taskcontroller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.dbentity.ProjectAssignment;
import com.zerp.taskmanagement.dto.ProjectDTO;
import com.zerp.taskmanagement.taskservice.ProjectService;

@RestController
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @PostMapping("/projects")
    public Project createProject(@RequestBody ProjectDTO project) {
        return projectService.createProject(project);
    }

    @PostMapping("/projects/{id}/assignees")
    public String createAssignee( @PathVariable long projectId ,@RequestBody List<String> emails) {
        return projectService.createAssignee(projectId, emails);
    }

    @GetMapping("/projects")
    public List<Project> getProjects(){
        return projectService.getProjects();
    }

}
