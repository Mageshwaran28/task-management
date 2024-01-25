package com.zerp.taskmanagement.taskcontroller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.dbentity.ProjectAssignments;
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

    @PostMapping("/projects/assignee")
    public ProjectAssignments createAssignee(@RequestBody ProjectAssignments projectAssignments) {
        return projectService.createAssignee(projectAssignments);
    }

    @GetMapping("/projects")
    public List<Project> getProjects(){
        return projectService.getProjects();
    }

}
