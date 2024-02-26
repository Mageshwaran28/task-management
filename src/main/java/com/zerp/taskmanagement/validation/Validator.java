package com.zerp.taskmanagement.validation;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zerp.taskmanagement.exceptions.InvalidInputException;
import com.zerp.taskmanagement.model.Task;
import com.zerp.taskmanagement.repository.FileRepository;
import com.zerp.taskmanagement.repository.ProjectAssignmentRepository;
import com.zerp.taskmanagement.repository.ProjectRepository;
import com.zerp.taskmanagement.repository.TaskAssignmentRepository;
import com.zerp.taskmanagement.repository.TaskRepository;
import com.zerp.taskmanagement.repository.UserRepository;
import com.zerp.taskmanagement.service.JwtService;

@Component
public class Validator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    private static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{6,16}$";

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

    @Autowired
    JwtService jwtService;

    public boolean isValidUser(Long id) {
        if (userRepository.existsById(id)) {
            return true;
        } else {
            throw new InvalidInputException("Invalid user id : " + id);
        }
    }

    public boolean isValidProject(long projectId) {
        if (projectRepository.existsById(projectId)) {
            return true;
        } else {
            throw new InvalidInputException("Invalid project id : " + projectId);
        }
    }

    public boolean isValidTask(long taskId) {
        if (taskRepository.existsById(taskId)) {
            return true;
        } else {
            throw new InvalidInputException("Invalid task id : " + taskId);
        }
    }

    public boolean isValidFile(long id) {
        if (fileRepository.existsById(id)) {
            return true;
        } else {
            throw new InvalidInputException("Invalid file id : " + id);
        }
    }

    public boolean isValidStartDate(LocalDateTime startDate) {
        if (startDate.isAfter(LocalDateTime.now()) || startDate.isEqual(LocalDateTime.now())) {
            return true;
        } else {
            throw new InvalidInputException("Invalid start date : " + startDate.toString());
        }
    }

    public boolean isValidDueDate(LocalDateTime dueDate) {
        if (dueDate.isAfter(LocalDateTime.now())) {
            return true;
        } else {
            throw new InvalidInputException("Inavalid due date : " + dueDate.toString());
        }
    }

    public boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) {
            return true;
        } else {
            throw new InvalidInputException("Invalid email format : " + email);
        }
    }

    public boolean isValidProjectAssignee(long id, String username) {
        long assigneeId = userRepository.findByEmailIgnoreCase(username).getId();
        return projectAssignmentRepository.existsByProjectIdAndAssigneeId(id, assigneeId);
    }

    public boolean isValidTaskAssignee(long id, String username) {
        long assigneeId = userRepository.findByEmailIgnoreCase(username).getId();
        return taskAssignmentRepository.existsByTaskIdAndAssigneeId(id, assigneeId);
    }

    public boolean isValidPassword(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        if (matcher.matches()) {
            return true;
        } else {
            throw new InvalidInputException(
                    "Password must be strong, it contains \natleast one lowercase character \natleast one uppercase character \natleast one digit \natleast one super character \ntotally min 6 and max 16 characters");
        }
    }

    public boolean isValidParentTask(long parentTaskId, long projectId) {
        Task parentTask = taskRepository.findById(parentTaskId).get();

        if (parentTask.getDepth() + 1 > 3) {
            throw new InvalidInputException("More than two levels of subtasks are not allowed.");
        }

        if (parentTask.getProject().getId() != projectId) {
            throw new InvalidInputException("The project id is must be same in both parent and child tasks.");
        }

        return true;
    }

    public boolean isAdmin(String username) {
        return username.equals("admin@zirius.in") ? true : false;
    }

    public boolean isAdminOrTaskCreator(Long id, String loginUser) {
        isValidTask(id);
        if (isAdmin(loginUser)) {
            return true;
        } else {
            long userId = userRepository.findByEmailIgnoreCase(loginUser).getId();
            return taskRepository.existsByIdAndCreatorId(id, userId);
        }
    }

    public boolean isAdminOrProjectCreator(Long id, String loginUser) {
        isValidProject(id);
        if (isAdmin(loginUser)) {
            return true;
        } else {
            long userId = userRepository.findByEmailIgnoreCase(loginUser).getId();
            return projectRepository.existsByIdAndCreatorId(id, userId);
        }
    }

}
