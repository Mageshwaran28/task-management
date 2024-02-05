package com.zerp.taskmanagement.taskcontroller;


import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.zerp.taskmanagement.taskservice.JwtService;
import com.zerp.taskmanagement.taskservice.ProjectService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    JwtService jwtService;

    @PostMapping("/projects")
    public Project createProject(@RequestBody ProjectDTO project, HttpServletRequest request) {
        return projectService.createProject(project, request);
    }

    @PostMapping("/projects/{id}/assignees")
    public String createAssignee( @PathVariable long id ,@RequestBody Set<Long> assigneesId, HttpServletRequest request) {
        return projectService.createAssignee(id, assigneesId, request);
    }

    @GetMapping("/projects")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Project> getProjects(){
        return projectService.getProjects();
    }

    @GetMapping("/projects/{id}")
    public Project getProject(@PathVariable long id , HttpServletRequest request){
        return projectService.getProject(id, request);
    }

    @GetMapping("projects/creators")
    public List<Project> getProjectsByCreatorId(HttpServletRequest request){
        return projectService.getProjectsByCreatorId(request);
    }

    @GetMapping("projects/assignees")
    public List<Project> getProjectsByAssignee(HttpServletRequest request){
        return projectService.getProjectsByAssigneeId(request);
    }

    @PutMapping("projects/{id}")
    public String updateProject(@PathVariable long id, @RequestBody ProjectUpdateDTO updateProject, HttpServletRequest request) {
        return projectService.updateProject(id, updateProject, request);
    }


    @DeleteMapping("projects/{id}")
    public String deleteProject(@PathVariable long id, HttpServletRequest request) {
        return projectService.deleteProject(id, request);
    }

    @DeleteMapping("projects/{id}/assignees/{assigneeId}")
    public String deleteProjectAssignee(@PathVariable long id, @PathVariable Long assigneeId, HttpServletRequest request){
        return projectService.deleteProjectAssignee(id, assigneeId, request);
    }

}
