package com.zerp.taskmanagement.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zerp.taskmanagement.dto.TaskDTO;
import com.zerp.taskmanagement.dto.TaskUpdateDTO;
import com.zerp.taskmanagement.exceptions.EmptyInputException;
import com.zerp.taskmanagement.exceptions.InvalidInputException;
import com.zerp.taskmanagement.model.File;
import com.zerp.taskmanagement.model.Project;
import com.zerp.taskmanagement.model.Task;
import com.zerp.taskmanagement.model.TaskAssignment;
import com.zerp.taskmanagement.model.User;
import com.zerp.taskmanagement.enums.Priority;
import com.zerp.taskmanagement.enums.Status;
import com.zerp.taskmanagement.repository.FileRepository;
import com.zerp.taskmanagement.repository.ProjectRepository;
import com.zerp.taskmanagement.repository.TaskAssignmentRepository;
import com.zerp.taskmanagement.repository.TaskRepository;
import com.zerp.taskmanagement.repository.UserRepository;
import com.zerp.taskmanagement.utils.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class TaskService extends CommonUtils {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    TaskAssignmentRepository taskAssignmentRepository;

    @Autowired
    MailService mailService;

    public Task createTask(TaskDTO taskDTO, String creator) {

        isFieldsAreEmpty(taskDTO);
        isValidDueDate(taskDTO.getDueDate());
        isValidStartDate(taskDTO.getStartDate());

        Task task =  new Task();
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());

        task.setPriority(getPriority(taskDTO.getPriority()));
        task.setStatus(getStatus(taskDTO.getStatus()));
        task.setCreatedAt(LocalDateTime.now());
        task.setStartDate(taskDTO.getStartDate());
        task.setDueDate(taskDTO.getDueDate());
        task.setCreator(getCreator(creator));
        task.setAssignees(getAssignees(taskDTO.getAssignees()));
        task.setProject(getProject(taskDTO.getProjectId()));

        if (taskDTO.getParentTaskId() != 0
                && isValidParentTask(taskDTO.getParentTaskId(), taskDTO.getProjectId())) {
            Task parentTask = taskRepository.findById(taskDTO.getParentTaskId()).get();
            int depth = parentTask.getDepth() + 1;
            task.setParentTask(parentTask);
            task.setDepth(depth);
        }

        taskRepository.save(task);

        mailService.acknwoledgeForCreatedTask(task);
        return task;
    }

   

    private Priority getPriority(int priority) {
        return Priority.fromString(Integer.toString(priority));
    }

    private Project getProject(long projectId) {
        isValidProject(projectId);
        return projectRepository.findById(projectId).get();
    }

    private boolean isFieldsAreEmpty(TaskDTO taskDTO) {
        if (taskDTO.getName() == null || taskDTO.getDescription() == null || taskDTO.getPriority() == 0
                || taskDTO.getStatus() == 0 || taskDTO.getStartDate() == null || taskDTO.getDueDate() == null
                || taskDTO.getAssignees() == null || taskDTO.getProjectId() == 0) {
            throw new EmptyInputException();
        }

        return false;
    }

    public List<Task> getTasks() {
        List<Task> tasks = taskRepository.findByParentTaskIdNull();
        System.out.println(tasks.get(0).getCreatedAt());
        System.out.println();

        if (tasks.size() == 0 || tasks == null) {
            throw new NoSuchElementException();
        }

        return tasks;
    }

    public File uploadFile(Long id, MultipartFile uploadFile, String loginUser) throws IOException {

        if (uploadFile.isEmpty()) {
            throw new InvalidInputException("Please choose a file to upload");
        }

        File file =  new File();
        file.setName(uploadFile.getOriginalFilename());
        file.setType(uploadFile.getContentType());
        file.setDocument(uploadFile.getBytes());
        file.setUploadedDate(LocalDateTime.now());
        file.setTask(taskRepository.findById(id).get());
        file.setUploadedBy(getCreator(loginUser));

        fileRepository.save(file);
        return file;
    }

    public String createAssignee(long id, Set<Long> assigneesId, HttpServletRequest request) {
        Set<User> users = getAssignees(assigneesId);

        for (User user : users) {
            if (!isValidTaskAssignee(id, user.getEmail())) {
                TaskAssignment assignment =  new TaskAssignment();
                assignment.setTask(taskRepository.findById(id).get());
                assignment.setAssignee(user);
                taskAssignmentRepository.save(assignment);
            } else {
                throw new InvalidInputException("Already assignee exists for task " + user.getId());
            }
        }

        return "Success";

    }

    public Task geTask(Long id, HttpServletRequest request) {
        return taskRepository.findById(id).get();
    }

    public List<Task> getTasksByPriority(Priority priority) {
        List<Task> tasks = taskRepository.findByPriority(priority);

        if (tasks.size() == 0) {
            throw new NoSuchElementException();
        }
        return tasks;

    }

    public List<Task> getTasksByStatus(Status status) {
        List<Task> tasks = taskRepository.findByStatus(status);

        if (tasks.size() == 0) {
            throw new NoSuchElementException();
        }
        return tasks;
    }

    public List<Task> getTasksByCreator(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        List<Task> tasks = taskRepository.findByCreatorId(user.getId());

        if (tasks.size() == 0) {
            throw new NoSuchElementException();
        }
        return tasks;
    }

    public List<Task> getTasksByAssignee(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        List<TaskAssignment> taskAssignments = taskAssignmentRepository.findByAssigneeId(user.getId());
        List<Task> tasks = new LinkedList<Task>();

        for (TaskAssignment taskAssignment : taskAssignments) {
            tasks.add(taskAssignment.getTask());
        }

        if (tasks.size() == 0) {
            throw new NoSuchElementException();
        }

        return tasks;
    }

    public List<Task> getTasksByDueDate() {
        List<Task> tasks = taskRepository.findByDueDateBefore(LocalDateTime.now());

        if (tasks.size() == 0) {
            throw new NoSuchElementException();
        }
        return tasks;
    }

    @Transactional
    public String deleteTaskAssigneesById(Long id, Long assigneeId) {

        User user = userRepository.findById(assigneeId).get();
        if (user != null && isValidTaskAssignee(id, user.getEmail())) {
            taskAssignmentRepository.deleteByTaskIdAndAssigneeId(id, user.getId());
        } else {
            throw new InvalidInputException("Invalid details");
        }

        return "Remove assignee " + assigneeId + " from task assignment " + id;
    }

    public String updateTaskStatus(Long id, Status status) {
        Task task = taskRepository.findById(id).get();
        task.setStatus(status);
        taskRepository.save(task);
        return "Task status updated successfully ";

    }

    public String updateTask(Long id, TaskUpdateDTO taskUpdateDTO) {

        Task task = taskRepository.findById(id).get();
        if (taskUpdateDTO.getParentTaskId() != 0 && taskUpdateDTO.getProjectId() != 0) {
            isValidParentTask(taskUpdateDTO.getParentTaskId(), taskUpdateDTO.getProjectId());
        }
        if (taskUpdateDTO.getName() != null) {
            task.setName(taskUpdateDTO.getName());
        }
        if (taskUpdateDTO.getDescription() != null) {
            task.setDescription(taskUpdateDTO.getDescription());
        }
        if (taskUpdateDTO.getDueDate() != null && isValidDueDate(taskUpdateDTO.getDueDate())) {
            task.setDueDate(taskUpdateDTO.getDueDate());
        }
        if (taskUpdateDTO.getParentTaskId() != 0) {
            task.setParentTask(taskRepository.findById(taskUpdateDTO.getParentTaskId()).get());
        }
        if (taskUpdateDTO.getProjectId() != 0) {
            task.setProject(projectRepository.findById(taskUpdateDTO.getProjectId()).get());
        }

        taskRepository.save(task);

        return "Task Updated";

    }

}
