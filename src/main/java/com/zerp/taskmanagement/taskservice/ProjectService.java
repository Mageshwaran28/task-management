package com.zerp.taskmanagement.taskservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.dbrepository.ProjectRepository;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }
 

}
