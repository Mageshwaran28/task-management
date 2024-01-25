package com.zerp.taskmanagement.taskservice;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.customexception.EmptyInputException;
import com.zerp.taskmanagement.customexception.InvalidInputException;
import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.dbentity.ProjectAssignments;
import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dbrepository.ProjectAssignmentsRepository;
import com.zerp.taskmanagement.dbrepository.ProjectRepository;
import com.zerp.taskmanagement.dbrepository.UserRepository;
import com.zerp.taskmanagement.dto.ProjectDTO;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectAssignmentsRepository assignmentsRepository;

    public Project createProject(ProjectDTO projectDTO) {
        if (isFieldsAreEmpty(projectDTO)) {
            throw new EmptyInputException();
        }

        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());

        User creator = userRepository.findByEmailIgnoreCase(projectDTO.getCreator());
        if(creator == null){
            System.out.println(projectDTO.getCreator());
            throw new InvalidInputException("Invalid creator email address ");
        }

        project.setCreator(creator);
        project.setAssignees(getAssignees(projectDTO.getAssignees()));        
        projectRepository.save(project);
        return project;

    }

    private List<User> getAssignees(List<String> assigneesName) {
        List<User> assignees = new LinkedList<User>();
        Iterator<String> it = assigneesName.iterator();

        while (it.hasNext()) {
            User assignee = userRepository.findByEmailIgnoreCase(it.next());
            if(assignee == null){
                throw new InvalidInputException("Invalid assignee email address: ");
            }
            assignees.add(assignee);
        }

        return assignees;
    }

    private boolean isFieldsAreEmpty(ProjectDTO projectDTO) {

        if (projectDTO.getName() == null || projectDTO.getDescription() == null || projectDTO.getCreator() == null
                || projectDTO.getAssignees()== null || projectDTO.getAssignees().size() == 0) {
            return true;
        }

        return false;
    }

    public ProjectAssignments createAssignee(ProjectAssignments projectAssignments) {
        assignmentsRepository.save(projectAssignments);
        return projectAssignments;
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    // public List<Project> getProjects() {
    // List<Project> projects = projectRepository.findAll();

    // if (projects.size() == 0) {
    // throw new NoSuchElementException("No value is present in database , Please
    // change your request");
    // }

    // return projects;
    // }


    // public Project findByprojectId(long projectId) {
    // Project project = projectRepository.findByProjectId(projectId);

    // if (project == null) {
    // throw new NoSuchElementException("No value is present in database , Please
    // change your request");
    // }

    // return project;
    // }

    // public String updateProject(Project project, long projectId) {

    // Project existProject = projectRepository.findByProjectId(projectId);

    // if (project.getProjectName() != null &&
    // existProject.getProjectName().length() != 0) {
    // existProject.setProjectName(project.getProjectName());
    // }
    // if (project.getProjectDescription() != null &&
    // existProject.getProjectDescription().length() != 0) {
    // existProject.setProjectDescription(project.getProjectDescription());
    // }
    // if (project.getStartDate() != null) {
    // existProject.setStartDate(project.getStartDate());
    // }
    // if (project.getEndDate() != null) {
    // existProject.setEndDate(project.getEndDate());
    // }

    // projectRepository.save(existProject);

    // return "Updated";
    // }
}
