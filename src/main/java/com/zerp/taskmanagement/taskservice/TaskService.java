package com.zerp.taskmanagement.taskservice;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zerp.taskmanagement.customexception.EmptyInputException;
import com.zerp.taskmanagement.customexception.InvalidInputException;
import com.zerp.taskmanagement.customexception.UnAuthorizeException;
import com.zerp.taskmanagement.dbentity.File;
import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.dbentity.Task;
import com.zerp.taskmanagement.dbentity.TaskAssignment;
import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dbrepository.FileRepository;
import com.zerp.taskmanagement.dbrepository.ProjectRepository;
import com.zerp.taskmanagement.dbrepository.TaskAssignmentRepository;
import com.zerp.taskmanagement.dbrepository.TaskRepository;
import com.zerp.taskmanagement.dbrepository.UserRepository;
import com.zerp.taskmanagement.dto.TaskDTO;
import com.zerp.taskmanagement.dto.TaskUpdateDTO;
import com.zerp.taskmanagement.myenum.Priority;
import com.zerp.taskmanagement.myenum.Status;
import com.zerp.taskmanagement.validation.Validator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class TaskService {

    @Autowired
    Validator validator;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    TaskAssignmentRepository taskAssignmentRepository;

    public Task createTask(TaskDTO taskDTO, HttpServletRequest request) {

        if (isFieldsAreEmpty(taskDTO)) {
            throw new EmptyInputException();
        }

        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());

        task.setPriority(getPriority(taskDTO.getPriority()));
        task.setStatus(getStatus(taskDTO.getStatus()));
        task.setCreatedAt(LocalDateTime.now());

        if (validator.isValidStartDate(taskDTO.getStartDate())) {
            task.setStartDate(taskDTO.getStartDate());
        }

        if (validator.isValidDueDate(taskDTO.getDueDate())) {
            task.setDueDate(taskDTO.getDueDate());
        }

        task.setCreator(projectService.getCreator(validator.getUserEmail(request)));
        task.setAssignees(projectService.getAssignees(taskDTO.getAssignees()));
        task.setProject(getProject(taskDTO.getProjectId()));

        if (taskDTO.getParentTaskId() != 0
                && validator.isValidParentTask(taskDTO.getParentTaskId(), taskDTO.getProjectId())) {
            Task parentTask = taskRepository.findById(taskDTO.getParentTaskId()).get();
            int depth = parentTask.getDepth() + 1;
            task.setParentTask(parentTask);
            task.setDepth(depth);
        }

        taskRepository.save(task);

        return task;

    }

    private Status getStatus(int status) {
        return Status.fromString(Integer.toString(status));
    }

    private Priority getPriority(int priority) {
        return Priority.fromString(Integer.toString(priority));
    }

    private Project getProject(long projectId) {
        validator.isValidProject(projectId);
        return projectRepository.findById(projectId).get();
    }

    private boolean isFieldsAreEmpty(TaskDTO taskDTO) {
        if (taskDTO.getName() == null || taskDTO.getDescription() == null || taskDTO.getPriority() == 0
                || taskDTO.getStatus() == 0 || taskDTO.getStartDate() == null || taskDTO.getDueDate() == null
                || taskDTO.getAssignees() == null || taskDTO.getProjectId() == 0) {
            return true;
        }

        return false;
    }

    public List<Task> getTasks() {

        List<Task> tasks = taskRepository.findByParentTaskIdNull();

        if (tasks.size() == 0 || tasks == null) {
            throw new NoSuchElementException();
        }

        return tasks;
    }

    public File uploadFile(Long taskId, MultipartFile uploadFile, HttpServletRequest request) throws IOException {

        if (uploadFile.isEmpty()) {
            throw new InvalidInputException("Please choose a file to upload");
        }

        validator.isValidTask(taskId);

        String loginUser = validator.getUserEmail(request);
        Task task = taskRepository.findById(taskId).get();
        if (loginUser.equals(task.getCreator().getEmail()) || validator.isExistAssignee(task, loginUser)) {
            File file = new File();
            file.setName(uploadFile.getOriginalFilename());
            file.setType(uploadFile.getContentType());
            file.setDocument(uploadFile.getBytes());
            file.setUploadedDate(LocalDateTime.now());
            file.setTask(taskRepository.findById(taskId).get());
            file.setUploadedBy(projectService.getCreator(loginUser));

            fileRepository.save(file);

            return file;
        }

        throw new UnAuthorizeException("Don't have permission to upload file");

    }

    public File getFile(Long id, HttpServletRequest request) {

        File recievedFile = fileRepository.findById(id).get();

        if (recievedFile == null) {
            throw new InvalidInputException("File not found: " + id);
        }

        Task task = recievedFile.getTask();
        String loginUser = validator.getUserEmail(request);
        if (loginUser.equals(task.getCreator().getEmail()) || validator.isExistAssignee(task, loginUser)) {
            return recievedFile;
        }

        throw new UnAuthorizeException("Don't have permission to view this file " + id);

    }

    public String createAssignee(long id, Set<Long> assigneesId, HttpServletRequest request) {

        Task task = taskRepository.findById(id).get();

        if (task != null) {
            String loginUser = validator.getUserEmail(request);
            if (!loginUser.equals(task.getCreator().getEmail())) {
                throw new UnAuthorizeException("Don't have permission to create assignees for this task." + id);
            }

            Set<User> users = projectService.getAssignees(assigneesId);

            for (User user : users) {
                if (!validator.isValidTaskAssignee(id, user.getId())) {
                    TaskAssignment assignment = new TaskAssignment();
                    assignment.setTask(taskRepository.findById(id).get());
                    assignment.setAssignee(user);
                    taskAssignmentRepository.save(assignment);
                } else {
                    throw new InvalidInputException("Already assignee exists for task " + user.getEmail());
                }
            }

        }

        return "Success";

    }

    public Task geTask(Long id, HttpServletRequest request) {
        Task task = taskRepository.findById(id).get();

        if (task == null) {
            throw new NoSuchElementException();
        }
        String loginUser = validator.getUserEmail(request);
        if (loginUser.equals(task.getCreator().getEmail()) || validator.isExistAssignee(task, loginUser)) {
            return task;
        }

        throw new UnAuthorizeException("Don't have permission to view this task" + id);
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

    public List<Task> getTasksByCreatorId(HttpServletRequest request) {

        String email = validator.getUserEmail(request);

        User user = userRepository.findByEmailIgnoreCase(email);
        List<Task> tasks = taskRepository.findByCreatorId(user.getId());

        if (tasks.size() == 0) {
            throw new NoSuchElementException();
        }
        return tasks;
    }

    public List<Task> getTasksByAssigneeId(HttpServletRequest request) {
        String email = validator.getUserEmail(request);
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

    public String deleteTaskById(Long id, HttpServletRequest request) {

        Task task = taskRepository.findById(id).get();
        if (task != null) {
            String loginUser = validator.getUserEmail(request);
            if (!loginUser.equals(task.getCreator().getEmail())) {
                throw new UnAuthorizeException("Don't have permission to delete this task " + id);
            }

            taskRepository.deleteById(id);
        } else {
            throw new InvalidInputException("Invalid task id: " + id);
        }

        return "Task " + id + " has been deleted";
    }

    public String deleteTaskFileById(Long id, HttpServletRequest request) {
        File file = fileRepository.findById(id).get();
        if (file != null) {

            String loginUser = validator.getUserEmail(request);
            if (!loginUser.equals(file.getUploadedBy().getEmail())) {
                throw new UnAuthorizeException("Don't have permission to delete this file" + id);
            }

            fileRepository.deleteById(id);
        } else {
            throw new InvalidInputException("Invalid file id: " + id);
        }

        return "File " + id + " has been deleted";
    }

    @Transactional
    public String deleteTaskAssigneesById(Long id, Long assigneeId, HttpServletRequest request) {

        User user = userRepository.findById(assigneeId).get();
        Task task = taskRepository.findById(id).get();
        if (user != null && validator.isValidTaskAssignee(id, user.getId())) {
            String loginUser = validator.getUserEmail(request);

            if (!loginUser.equals(task.getCreator().getEmail())) {
                throw new UnAuthorizeException("Don't have permission to delete assignee from this task" + id);
            }

            taskAssignmentRepository.deleteByTaskIdAndAssigneeId(id, user.getId());
        } else {
            throw new InvalidInputException("Invalid details");
        }

        return "Remove assignee " + assigneeId + " from task assignment " + id;
    }

    public String updateTaskStatus(Long id, Status status, HttpServletRequest request) {

        Task task = taskRepository.findById(id).get();
        if (task != null) {
            String loginUser = validator.getUserEmail(request);
            if (loginUser.equals(task.getCreator().getEmail()) || validator.isExistAssignee(task, loginUser)) {
                task.setStatus(status);
                taskRepository.save(task);
                return "Task status updated successfully ";
            }

            throw new UnAuthorizeException("Don't have permission to update status of this task." + id);

        } else {
            throw new InvalidInputException("Task not found");
        }

    }

    public String updateTask(Long id, TaskUpdateDTO taskUpdateDTO, HttpServletRequest request) {

        Task task = taskRepository.findById(id).get();

        if (task != null) {

            String loginUser = validator.getUserEmail(request);
            if(!loginUser.equals(task.getCreator().getEmail()) ){
                throw new InvalidInputException("Don't have permission to update this task. "+id);
            }

            if (taskUpdateDTO.getParentTaskId() != 0 && taskUpdateDTO.getProjectId() != 0) {
                validator.isValidParentTask(taskUpdateDTO.getParentTaskId(), taskUpdateDTO.getProjectId());
            }
            if (taskUpdateDTO.getName() != null) {
                task.setName(taskUpdateDTO.getName());
            }
            if (taskUpdateDTO.getDescription() != null) {
                task.setDescription(taskUpdateDTO.getDescription());
            }
            if (taskUpdateDTO.getDueDate() != null && validator.isValidDueDate(taskUpdateDTO.getDueDate())) {
                task.setDueDate(taskUpdateDTO.getDueDate());
            }
            if (taskUpdateDTO.getParentTaskId() != 0) {
                task.setParentTask(taskRepository.findById(taskUpdateDTO.getParentTaskId()).get());
            }
            if (taskUpdateDTO.getProjectId() != 0) {
                task.setProject(projectRepository.findById(taskUpdateDTO.getProjectId()).get());
            }

            taskRepository.save(task);

        } else {
            throw new InvalidInputException("Task not found");
        }

        return "Task Updated";

    }

}
