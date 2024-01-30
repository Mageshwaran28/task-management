package com.zerp.taskmanagement.taskservice;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerp.taskmanagement.customexception.EmptyInputException;
import com.zerp.taskmanagement.customexception.InvalidInputException;
import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.dbentity.ProjectAssignment;
import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dbrepository.ProjectAssignmentRepository;
import com.zerp.taskmanagement.dbrepository.ProjectRepository;
import com.zerp.taskmanagement.dbrepository.UserRepository;
import com.zerp.taskmanagement.dto.ProjectDTO;
import com.zerp.taskmanagement.dto.ProjectUpdateDTO;
import com.zerp.taskmanagement.validation.Validator;

@Service
public class ProjectService {

    @Autowired
    Validator validator;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectAssignmentRepository projectAssignmentRepository;

    public Project createProject(ProjectDTO projectDTO) {
        if (isFieldsAreEmpty(projectDTO)) {
            throw new EmptyInputException();
        }

        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());

        project.setCreator(getCreator(projectDTO.getCreator()));
        project.setAssignees(getAssignees(projectDTO.getAssignees()));
        projectRepository.save(project);
        return project;

    }

    public User getCreator(String creatorEmail){
        validator.isValidUser(creatorEmail);
        return userRepository.findByEmailIgnoreCase(creatorEmail);
    }

    public Set<User> getAssignees(Set<String> assigneesName) {
        Set<User> assignees = new HashSet<User>();

        for (String email : assigneesName) {
            validator.isValidUser(email);
            assignees.add(userRepository.findByEmailIgnoreCase(email));
        }

        return assignees;
    }

    private boolean isFieldsAreEmpty(ProjectDTO projectDTO) {

        if (projectDTO.getName() == null || projectDTO.getDescription() == null || projectDTO.getCreator() == null
                || projectDTO.getAssignees() == null || projectDTO.getAssignees().size() == 0) {
            return true;
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

    public String createAssignee(long projectId, Set<String> emails) {

        if (validator.isValidProject(projectId)) {

            Set<User> users = getAssignees(emails);

            for (User user : users) {
                if (!validator.isValidProjectAssignee(projectId, user.getId())) {
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

    public Project getProject(long id) {
        Project project = projectRepository.findById(id).get();

        if (project == null) {
            throw new NoSuchElementException();
        }

        return project;
    }

    public List<Project> getProjectsByCreatorId(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);

        List<Project> projects = projectRepository.findByCreatorId(user.getId());

        if (projects.size() == 0) {
            throw new NoSuchElementException();
        }
        return projects;
    }

    public List<Project> getProjectsByAssigneeId(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);

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

    public String updateProject(long id, ProjectUpdateDTO updateProject) {

        Project project = projectRepository.findById(id).get();

        if (project == null) {
            throw new InvalidInputException("Invalid project id: " + id);
        }

        if (updateProject.getName() != null && updateProject.getName().length() > 0) {
            project.setName(updateProject.getName());
        }

        if (updateProject.getDescription() != null && updateProject.getDescription().length() > 0) {
            project.setDescription(updateProject.getDescription());
        }

        projectRepository.save(project);

        return "Project updated";
    }

    public String deleteProject(long id) {

        if (validator.isValidProject(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new InvalidInputException("Project not found " + id);
        }

        return "Project " + id + " has been deleted";
    }


    @Transactional
    public String deleteProjectAssignee(long id, String email) {

        User user = userRepository.findByEmailIgnoreCase(email);

        if (user != null && validator.isValidProjectAssignee(id, user.getId())) {
            projectAssignmentRepository.removeByProjectIdAndAssigneeId(id, user.getId());
        }else{
            throw new InvalidInputException("Invalid credentials");
        }

        return "Remove assignee" + email + " from project " + id;
    }

}
