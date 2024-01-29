package com.zerp.taskmanagement.validation;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zerp.taskmanagement.dbrepository.FileRepository;
import com.zerp.taskmanagement.dbrepository.ProjectAssignmentRepository;
import com.zerp.taskmanagement.dbrepository.ProjectRepository;
import com.zerp.taskmanagement.dbrepository.TaskAssignmentRepository;
import com.zerp.taskmanagement.dbrepository.TaskRepository;
import com.zerp.taskmanagement.dbrepository.UserRepository;

@Component
public class Validator {
    
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    private static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";

    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskAssignmentRepository taskAssignmentRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    ProjectAssignmentRepository projectAssignmentRepository;

    public boolean isValidUser(String email){
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public boolean isValidProject(long projectId){
        return projectRepository.existsById(projectId);
    }

    public boolean isValidTask(long taskId){
        return taskRepository.existsById(taskId);
    }

    public boolean isValidFile(long id){
        return fileRepository.existsById(id);
    }

    public boolean isValidStartDate(LocalDateTime startDate){
        return !startDate.isBefore(LocalDateTime.now());
    }

    public boolean isValidDueDate(LocalDateTime dueDate){
        return dueDate.isAfter(LocalDateTime.now());
    }

    public boolean isValidEmail(String email){
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidProjectAssignee(long id , long assignee){
        return projectAssignmentRepository.existsByProjectIdAndAssigneeId(id, assignee);
    }

    public boolean isValidTaskAssignee(long id , long assignee){
        return taskAssignmentRepository.existsByTaskIdAndAssigneeId(id, assignee);
    }

    public boolean isValidPassword(String password){
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches();
    }

}
