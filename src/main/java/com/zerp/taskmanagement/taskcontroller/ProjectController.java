package com.zerp.taskmanagement.taskcontroller;


import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.dto.ProjectDTO;
import com.zerp.taskmanagement.dto.ProjectUpdateDTO;
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
    public String createAssignee( @PathVariable long id ,@RequestBody Set<String> emails) {
        return projectService.createAssignee(id, emails);
    }

    @GetMapping("/projects")
    public List<Project> getProjects(){
        return projectService.getProjects();
    }

    @GetMapping("/projects/{id}")
    public Project getProject(@PathVariable long id){
        return projectService.getProject(id);
    }

    @GetMapping("projects/creators/{email}")
    public List<Project> getProjectsByCreatorId(@PathVariable String email){
        return projectService.getProjectsByCreatorId(email);
    }

    @GetMapping("projects/assignees/{email}")
    public List<Project> getProjectsByAssigneeId(@PathVariable String email){
        return projectService.getProjectsByAssigneeId(email);
    }

    @PutMapping("projects/{id}")
    public String updateProject(@PathVariable long id, @RequestBody ProjectUpdateDTO updateProject){
        return projectService.updateProject(id, updateProject);
    }


    // @DeleteMapping("projects/{id}")
    // public String deleteProject(@PathVariable long id){
    //     return projectService.deleteProject(id);
    // }

    // @DeleteMapping("projects/{id}/assignees/{email}")
    // public String deleteProjectAssignee(@PathVariable long id, @PathVariable String email){
    //     return projectService.deleteProjectAssignee(id, email);
    // }

}
