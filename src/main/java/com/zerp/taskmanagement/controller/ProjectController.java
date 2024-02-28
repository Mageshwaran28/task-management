package com.zerp.taskmanagement.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zerp.taskmanagement.dto.ProjectDTO;
import com.zerp.taskmanagement.dto.ProjectUpdateDTO;
import com.zerp.taskmanagement.enums.Status;
import com.zerp.taskmanagement.exceptions.UnAuthorizeException;
import com.zerp.taskmanagement.model.Project;
import com.zerp.taskmanagement.service.JwtService;
import com.zerp.taskmanagement.service.ProjectService;
import com.zerp.taskmanagement.utils.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ProjectController extends CommonUtils {

    @Autowired
    ProjectService projectService;

    @Autowired
    JwtService jwtService;

    @PostMapping("/projects")
    public Project createProject(@RequestBody ProjectDTO project, HttpServletRequest request) {
        return projectService.createProject(project, getUserEmail(request));
    }

    @PostMapping("/projects/{id}/assignees")
    public String createAssignee(@PathVariable long id, @RequestBody Set<Long> assigneesId,
            HttpServletRequest request) {
        if (!isAdminOrProjectCreator(id, getUserEmail(request))) {
            throw new UnAuthorizeException("Don't have permission to create project assignee");
        }
        return projectService.createAssignee(id, assigneesId);
    }

    @GetMapping("/projects")
    public List<Project> getProjects() {
        return projectService.getProjects();
    }

    @GetMapping("/projects/{id}")
    public Project getProject(@PathVariable long id, HttpServletRequest request) {
        String loginUser = getUserEmail(request);
        if (isAdminOrProjectCreator(id, loginUser) || isValidProjectAssignee(id, loginUser)) {
            return projectService.getProject(id);
        }
        throw new UnAuthorizeException("Don't have permission to view this project : " + id);
    }

    @GetMapping("projects/creators")
    public List<Project> getProjectsByCreatorId(HttpServletRequest request) {
        return projectService.getProjectsByCreatorId(getUserEmail(request));
    }

    @GetMapping("projects/assignees")
    public List<Project> getProjectsByAssignee(HttpServletRequest request) {
        return projectService.getProjectsByAssigneeId(getUserEmail(request));
    }

    @PutMapping("projects/{id}")
    public String updateProject(@PathVariable long id, @RequestBody ProjectUpdateDTO updateProject,
            HttpServletRequest request) {
        if (isAdminOrProjectCreator(id, getUserEmail(request))) {
            return projectService.updateProject(id, updateProject, request);
        }
        throw new UnAuthorizeException("Don't have permission to update project");
    }

    @PutMapping("/projects/{id}/status/{statusId}")
    public String updateProjectStatus(@PathVariable Long id, @PathVariable String statusId, HttpServletRequest request) {
        Status status = Status.fromString(statusId);
        String loginUser = getUserEmail(request);
        if (isAdminOrProjectCreator(id, loginUser) || isValidProjectAssignee(id, loginUser)) {
            return projectService.updateProjectStatus(id, status);
        }
        throw new UnAuthorizeException("Don't have permission to update status of this task." + id);

    }

    @DeleteMapping("projects/{id}/assignees/{assigneeId}")
    public String deleteProjectAssignee(@PathVariable long id, @PathVariable Long assigneeId,
            HttpServletRequest request) {
        if (isAdminOrProjectCreator(id, getUserEmail(request))) {
            return projectService.deleteProjectAssignee(id, assigneeId, request);
        }
        throw new UnAuthorizeException("Don't have permission to delete this project assignee");
    }

}
