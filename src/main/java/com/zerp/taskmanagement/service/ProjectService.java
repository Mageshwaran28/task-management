package com.zerp.taskmanagement.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerp.taskmanagement.dto.ProjectDTO;
import com.zerp.taskmanagement.dto.ProjectUpdateDTO;
import com.zerp.taskmanagement.exceptions.EmptyInputException;
import com.zerp.taskmanagement.exceptions.InvalidInputException;
import com.zerp.taskmanagement.exceptions.UnAuthorizeException;
import com.zerp.taskmanagement.model.Project;
import com.zerp.taskmanagement.model.ProjectAssignment;
import com.zerp.taskmanagement.model.User;
import com.zerp.taskmanagement.repository.ProjectAssignmentRepository;
import com.zerp.taskmanagement.repository.ProjectRepository;
import com.zerp.taskmanagement.repository.UserRepository;
import com.zerp.taskmanagement.utils.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ProjectService extends CommonUtils {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectAssignmentRepository projectAssignmentRepository;

    public Project createProject(ProjectDTO projectDTO, String creator) {
        isFieldsAreEmpty(projectDTO);

        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setCreator(getCreator(creator));
        project.setAssignees(getAssignees(projectDTO.getAssignees()));
        projectRepository.save(project);
        return project;

    }

    public User getCreator(String creatorEmail) {
        return userRepository.findByEmailIgnoreCase(creatorEmail);
    }

    public Set<User> getAssignees(Set<Long> assigneesId) {
        Set<User> assignees = new HashSet<User>();

        for (long id : assigneesId) {
            isValidUser(id);
            assignees.add(userRepository.findById(id).get());
        }
        return assignees;
    }

    private boolean isFieldsAreEmpty(ProjectDTO projectDTO) {

        if (projectDTO.getName() == null || projectDTO.getDescription() == null
                || projectDTO.getAssignees() == null || projectDTO.getAssignees().size() == 0) {
            throw new EmptyInputException();
        }

        return false;
    }

    public List<Project> getProjects() {

        List<Project> projects = projectRepository.findAll();

        if (projects == null || projects.size() == 0) {
            throw new NoSuchElementException();
        }

        return projects;
    }

    public String createAssignee(long projectId, Set<Long> assigneesId) {

        if (isValidProject(projectId)) {
            Set<User> users = getAssignees(assigneesId);

            for (User user : users) {
                if (!isValidProjectAssignee(projectId, user.getEmail())) {
                    ProjectAssignment assignment = new ProjectAssignment();
                    assignment.setProject(projectRepository.findById(projectId).get());
                    assignment.setAssignee(user);
                    projectAssignmentRepository.save(assignment);
                } else {
                    throw new InvalidInputException("Already assignee exists for project " + user.getEmail());
                }
            }

        }

        return "Successfully";
    }

    public Project getProject(long id, HttpServletRequest request) {
        Project project = projectRepository.findById(id).get();

        if (project == null) {
            throw new NoSuchElementException();
        }
        String longinUser = getUserEmail(request);
        if (!longinUser.equals(project.getCreator().getEmail())) {
            throw new UnAuthorizeException("Don't have permission to view this project" + id);
        }

        return project;
    }

    public List<Project> getProjectsByCreatorId(String loginUser) {

        User user = userRepository.findByEmailIgnoreCase(loginUser);
        List<Project> projects = projectRepository.findByCreatorId(user.getId());

        if (projects.size() == 0) {
            throw new NoSuchElementException();
        }
        return projects;
    }

    public List<Project> getProjectsByAssigneeId(String loginUser) {
        User user = userRepository.findByEmailIgnoreCase(loginUser);

        List<ProjectAssignment> projectAssignments = projectAssignmentRepository.findByAssigneeId(user.getId());
        List<Project> projects = new LinkedList<Project>();

        for (ProjectAssignment projectAssignment : projectAssignments) {
            projects.add(projectAssignment.getProject());
        }

        if (projects.size() == 0) {
            throw new NoSuchElementException();
        }

        return projects;
    }

    public String updateProject(long id, ProjectUpdateDTO updateProject, HttpServletRequest request) {
        isValidProject(id);
        Project project = projectRepository.findById(id).get();

        if (updateProject.getName() != null && updateProject.getName().length() > 0) {
            project.setName(updateProject.getName());
        }

        if (updateProject.getDescription() != null && updateProject.getDescription().length() > 0) {
            project.setDescription(updateProject.getDescription());
        }

        projectRepository.save(project);

        return "Project updated";
    }

    @Transactional
    public String deleteProjectAssignee(long id, Long assigneeId, HttpServletRequest request) {
        String assignee = userRepository.findById(assigneeId).get().getEmail();
        if (isValidProjectAssignee(id, assignee)) {
            projectAssignmentRepository.removeByProjectIdAndAssigneeId(id, assigneeId);
        } else {
            throw new InvalidInputException("Invalid credentials");
        }

        return "Remove assignee" + assigneeId + " from project " + id;
    }

}
