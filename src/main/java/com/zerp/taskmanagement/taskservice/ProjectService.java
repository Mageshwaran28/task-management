package com.zerp.taskmanagement.taskservice;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.dbrepository.ProjectRepository;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public List<Project> getProjects() {
       List<Project> projects = projectRepository.findAll();
        
        if(projects.size() == 0) {
            throw new NoSuchElementException("No value is present in database , Please change your request" );
        }

        return projects;
    }

    public ResponseEntity<String> addProject(Project project) {
            projectRepository.save(project);
            return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    public Project findByprojectId(long projectId) {
        Project project =  projectRepository.findByProjectId(projectId);
        
        if(project == null) {
            throw new NoSuchElementException("No value is present in database , Please change your request" );
        }

        return project;
    }
}
