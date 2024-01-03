package com.zerp.taskmanagement.taskservice;

import java.util.ArrayList;
import java.util.List;

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

    public ResponseEntity<List<Project>> getProjects() {
        try {
            return new ResponseEntity<>(projectRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addProject(Project project) {
            projectRepository.save(project);
            return new ResponseEntity<>("success", HttpStatus.CREATED);
    }
}
