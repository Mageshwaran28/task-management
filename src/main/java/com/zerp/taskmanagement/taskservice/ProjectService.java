package com.zerp.taskmanagement.taskservice;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.dbrepository.ProjectRepository;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public List<Project> getProjects() {
        List<Project> projects = projectRepository.findAll();

        if (projects.size() == 0) {
            throw new NoSuchElementException("No value is present in database , Please change your request");
        }

        return projects;
    }

    public String addProject(Project project) {
        projectRepository.save(project);
        return "success";
    }

    public Project findByprojectId(long projectId) {
        Project project = projectRepository.findByProjectId(projectId);

        if (project == null) {
            throw new NoSuchElementException("No value is present in database , Please change your request");
        }

        return project;
    }

    public String updateProject(Project project, long projectId) {

        Project existProject = projectRepository.findByProjectId(projectId);

        if (project.getProjectName() != null && existProject.getProjectName().length() != 0) {
            existProject.setProjectName(project.getProjectName());
        }
        if (project.getProjectDescription() != null && existProject.getProjectDescription().length() != 0) {
            existProject.setProjectDescription(project.getProjectDescription());
        }
        if (project.getStartDate() != null) {
            existProject.setStartDate(project.getStartDate());
        }
        if (project.getEndDate() != null) {
            existProject.setEndDate(project.getEndDate());
        }

        projectRepository.save(existProject);

        return "Updated";
    }
}
