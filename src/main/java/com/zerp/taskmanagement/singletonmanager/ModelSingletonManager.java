package com.zerp.taskmanagement.singletonmanager;

import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.model.File;
import com.zerp.taskmanagement.model.Project;
import com.zerp.taskmanagement.model.ProjectAssignment;
import com.zerp.taskmanagement.model.Role;
import com.zerp.taskmanagement.model.Task;
import com.zerp.taskmanagement.model.TaskAssignment;
import com.zerp.taskmanagement.model.User;

@Service
public class ModelSingletonManager {

    private File fileInstance;
    private Project projectInstance;
    private ProjectAssignment projectAssignmentInstance;
    private Role roleInstance;
    private Task taskInstance;
    private TaskAssignment taskAssignmentInstance;
    private User userInstance;

    public ModelSingletonManager() {
        fileInstance = new File();
        projectInstance = new Project();
        projectAssignmentInstance = new ProjectAssignment();
        roleInstance = new Role();
        taskInstance = new Task();
        taskAssignmentInstance = new TaskAssignment();
        userInstance = new User();
    }

    public File getFileInstance() {
        return fileInstance;
    }

    public Project getProjectInstance() {
        return projectInstance;
    }

    public ProjectAssignment getProjectAssignmentInstance() {
        return projectAssignmentInstance;
    }

    public Role getRoleInstance() {
        return roleInstance;
    }

    public Task getTaskInstance() {
        return taskInstance;
    }

    public TaskAssignment getTaskAssignmentInstance() {
        return taskAssignmentInstance;
    }

    public User getUserInstance() {
        return userInstance;
    }

}
