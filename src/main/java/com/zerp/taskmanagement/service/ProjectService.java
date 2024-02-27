package com.zerp.taskmanagement.service;

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
import com.zerp.taskmanagement.model.Project;
import com.zerp.taskmanagement.model.ProjectAssignment;
import com.zerp.taskmanagement.model.User;
import com.zerp.taskmanagement.repository.ProjectAssignmentRepository;
import com.zerp.taskmanagement.repository.ProjectRepository;
import com.zerp.taskmanagement.repository.UserRepository;
import com.zerp.taskmanagement.singletonmanager.CollectionSingletonManager;
import com.zerp.taskmanagement.singletonmanager.ModelSingletonManager;
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

    @Autowired
    ModelSingletonManager modelSingletonManager;

    @Autowired
    CollectionSingletonManager collectionSingletonManager;

    public Project createProject(ProjectDTO projectDTO, String creator) {
        isFieldsAreEmpty(projectDTO);

        Project project = modelSingletonManager.getProjectInstance();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setCreator(getCreator(creator));
        project.setAssignees(getAssignees(projectDTO.getAssignees()));
        projectRepository.save(project);
        return project;

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
                    ProjectAssignment assignment = modelSingletonManager.getProjectAssignmentInstance();
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

    public Project getProject(long id) {
        return projectRepository.findById(id).get();
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
        List<Project> projects = collectionSingletonManager.getProjectLinkedListInstance();

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
