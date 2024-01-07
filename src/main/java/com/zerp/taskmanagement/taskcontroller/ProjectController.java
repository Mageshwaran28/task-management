package com.zerp.taskmanagement.taskcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.taskservice.ProjectService;

@RestController
@RequestMapping(value = "/taskmanagement/")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping("/projects")
    public List<Project> getProjects() {
        return projectService.getProjects();
    }

    @GetMapping("/project/{projectId}")
    public Project findByprojectId(@PathVariable long projectId) {
        return projectService.findByprojectId(projectId);
    }

    @PostMapping("/project/add")
    public String addProject(@RequestBody Project project) {
        return projectService.addProject(project);
    }

    @PutMapping("/project/{projectId}")
    public String updateProject(@PathVariable long projectId, @RequestBody Project project) {
        return projectService.updateProject(project, projectId);
    }

}
