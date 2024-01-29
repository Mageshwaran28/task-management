package com.zerp.taskmanagement.taskservice;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zerp.taskmanagement.customexception.EmptyInputException;
import com.zerp.taskmanagement.customexception.InvalidInputException;
import com.zerp.taskmanagement.dbentity.File;
import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.dbentity.ProjectAssignment;
import com.zerp.taskmanagement.dbentity.Task;
import com.zerp.taskmanagement.dbentity.TaskAssignment;
import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dbrepository.FileRepository;
import com.zerp.taskmanagement.dbrepository.ProjectRepository;
import com.zerp.taskmanagement.dbrepository.TaskAssignmentRepository;
import com.zerp.taskmanagement.dbrepository.TaskRepository;
import com.zerp.taskmanagement.dbrepository.UserRepository;
import com.zerp.taskmanagement.dto.TaskDTO;
import com.zerp.taskmanagement.validation.Validator;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    Validator validator;

    @Autowired
    TaskAssignmentRepository taskAssignmentRepository;

    public Task createTask(TaskDTO taskDTO) {

        if (isFieldsAreEmpty(taskDTO)) {
            throw new EmptyInputException();
        }

        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setPriority(taskDTO.getPriority());
        task.setStatus(taskDTO.getStatus());
        task.setCreatedAt(LocalDateTime.now());
        task.setStartDate(taskDTO.getStartDate());
        task.setDueDate(taskDTO.getDueDate());

        task.setCreator(userRepository.findByEmailIgnoreCase(taskDTO.getCreator()));
        task.setAssignees(getAssignees(taskDTO.getAssignees()));
        task.setProject(getProject(taskDTO.getProjectId()));

        if (taskDTO.getParentTaskId() != 0 && validator.isValidTask(taskDTO.getParentTaskId())) {
            Task parentTask = taskRepository.findById(taskDTO.getParentTaskId()).get();
            int depth = parentTask.getDepth() + 1;

            if (depth > 3) {
                throw new InvalidInputException("More than two levels of subtasks are not allowed.");
            }
            task.setParentTask(parentTask);
            task.setDepth(depth);
        }

        System.out.println(task.toString());

        taskRepository.save(task);

        return task;

    }

    private Project getProject(long projectId) {
        Project project = projectRepository.findById(projectId).get();

        if (project == null) {
            throw new InvalidInputException("Project id not found");
        }


        return project;
    }

    private List<User> getAssignees(List<String> assigneesName) {
        List<User> assignees = new LinkedList<User>();
        Iterator<String> it = assigneesName.iterator();

        while (it.hasNext()) {
            User assignee = userRepository.findByEmailIgnoreCase(it.next());
            if (assignee == null) {
                throw new InvalidInputException("Invalid creator name ");
            }
            assignees.add(assignee);
        }

        return assignees;
    }

    private boolean isFieldsAreEmpty(TaskDTO taskDTO) {
        System.out.println(taskDTO.getName());
        System.out.println(taskDTO.getDescription());
        System.out.println(taskDTO.getCreator());
        System.out.println(taskDTO.getProjectId());
        System.out.println(taskDTO.getPriority());
        System.out.println(taskDTO.getStatus());
        System.out.println(taskDTO.getStartDate());
        System.out.println(taskDTO.getDueDate());
        System.out.println(taskDTO.getAssignees());
        if (taskDTO.getName() == null || taskDTO.getDescription() == null || taskDTO.getPriority() == null
                || taskDTO.getStatus() == null || taskDTO.getStartDate() == null || taskDTO.getDueDate() == null
                || taskDTO.getCreator() == null || taskDTO.getAssignees() == null || taskDTO.getProjectId() == 0) {
            return true;
        }

        return false;
    }

    public List<Task> getTasks() {

        List<Task> tasks = taskRepository.findByParentTaskIdNull();

        if (tasks.size() == 0) {
            throw new NoSuchElementException();
        }

        return tasks;
    }

    public File uploadFile(Long taskId, MultipartFile uploadFile) throws IOException {

        if (uploadFile.isEmpty()) {
            throw new InvalidInputException("Please choose a file to upload");
        }

        File file = new File();
        file.setName(uploadFile.getOriginalFilename());
        file.setType(uploadFile.getContentType());
        file.setDocument(uploadFile.getBytes());
        file.setUploadedDate(LocalDateTime.now());

        Task task = taskRepository.findById(taskId).get();
        if (task != null) {
            file.setTask(task);
        } else {
            throw new InvalidInputException("Could not find task");
        }

        fileRepository.save(file);

        return file;
    }

    public File getFile(Long id) {

        File recievedFile = fileRepository.findById(id).get();
        return recievedFile;
    }

    public TaskAssignment createAssignee(TaskAssignment taskAssignment) {
        return taskAssignmentRepository.save(taskAssignment);
    }

    // public String addTask(TaskDTO taskDTO) {
    // Task task = new Task();
    // isValidateTask(taskDTO);
    // isValidDueDate(taskDTO.getDueDate());
    // isValidateCreatorAndAssignee(taskDTO);
    // isValidateProject(taskDTO);

    // task.setTaskId(taskDTO.getTaskId());
    // task.setTaskTitle(taskDTO.getTaskTitle());
    // task.setTaskDescription(taskDTO.getTaskDescription());
    // task.setPriority(taskDTO.getPriority());
    // task.setDueDate(taskDTO.getDueDate());
    // task.setStatus(taskDTO.getStatus());
    // createTask(task, taskDTO);
    // taskRepository.save(task);
    // return "success";
    // }

    // private Task createTask(Task task, TaskDTO taskDTO) {
    // User creator = userRepository.findById(taskDTO.getCreatorId()).get();
    // User assignee = userRepository.findById(taskDTO.getAssigneeId()).get();
    // Project project = projectRepository.findById(taskDTO.getProjectId()).get();
    // task.setProject(project);
    // task.setCreator(creator);
    // task.setAssignee(assignee);
    // return task;
    // }

    // private boolean isValidateProject(TaskDTO taskDTO) {
    // Project project = projectRepository.findByProjectId(taskDTO.getProjectId());
    // if (project == null) {
    // throw new InvalidInputException("601", "Invalid project input , please check
    // your input");
    // }

    // return true;
    // }

    // private boolean isValidateCreatorAndAssignee(TaskDTO taskDTO) {
    // User creator = userRepository.findByUserId(taskDTO.getCreatorId());
    // User assignee = userRepository.findByUserId(taskDTO.getAssigneeId());
    // if (creator == null || assignee == null) {
    // throw new InvalidInputException("601", "Invalid creator or assignee input ,
    // please check your input");

    // } else if (taskDTO.getCreatorId() == taskDTO.getAssigneeId()) {
    // throw new InvalidInputException("601", "Invalid creator or assignee input ,
    // please check your input");
    // }

    // return true;
    // }

    // private boolean isValidateTask(TaskDTO taskDTO) {

    // if (taskDTO.getTaskTitle().isEmpty() ||
    // taskDTO.getTaskTitle().length() == 0 ||
    // taskDTO.getTaskDescription().isEmpty() ||
    // taskDTO.getTaskDescription().length() == 0) {
    // throw new InvalidInputException("601", "Inavalid input task , please check
    // your input");
    // }
    // return true;
    // }

    // private boolean isValidDueDate(Date dueDate) {
    // if (!(dueDate != null && dueDate.after(new Date()))) {
    // throw new InvalidInputException("601", "Inavalid due date , please check your
    // input");
    // }
    // return true;

    // }

    // public Task findByTaskId(long taskId) {
    // Task task = taskRepository.findByTaskId(taskId);
    // if (task == null) {
    // throw new NoSuchElementException("No value is present in database , Please
    // change your request");
    // }
    // return task;
    // }

    // public List<Task> findByStatus(Status status) {
    // List<Task> task = taskRepository.findByStatus(status);
    // if (task.size() == 0) {
    // throw new NoSuchElementException("No value is present in database , Please
    // change your request");
    // }
    // return task;
    // }

    // public List<Task> findByPriority(Priority priority) {
    // List<Task> task = taskRepository.findByPriority(priority);
    // if (task.size() == 0) {
    // throw new NoSuchElementException("No value is present in database , Please
    // change your request");
    // }
    // return task;
    // }

    // public String updateTask(long taskId, TaskDTO taskDTO) {

    // Task existingTask = taskRepository.findByTaskId(taskId);

    // if (taskDTO.getCreatorId() == 0) {
    // taskDTO.setCreatorId(existingTask.getCreator().getUserId());
    // }
    // if (taskDTO.getAssigneeId() == 0) {
    // taskDTO.setAssigneeId(existingTask.getAssignee().getUserId());
    // }

    // if (taskDTO.getTaskTitle() != null && taskDTO.getTaskTitle().length() != 0)
    // existingTask.setTaskTitle(taskDTO.getTaskTitle());

    // if (taskDTO.getTaskDescription() != null &&
    // taskDTO.getTaskDescription().length() != 0)
    // existingTask.setTaskDescription(taskDTO.getTaskDescription());

    // if (isValidateCreatorAndAssignee(taskDTO)) {
    // existingTask.setCreator(userRepository.findById(taskDTO.getCreatorId()).get());
    // existingTask.setAssignee(userRepository.findById(taskDTO.getAssigneeId()).get());
    // }
    // if (taskDTO.getProjectId() != 0) {
    // isValidateProject(taskDTO);
    // existingTask.setProject(projectRepository.findById(taskDTO.getProjectId()).get());
    // }
    // if (taskDTO.getDueDate() != null) {
    // isValidDueDate(taskDTO.getDueDate());
    // existingTask.setDueDate(taskDTO.getDueDate());
    // }
    // if (taskDTO.getPriority() != null) {
    // existingTask.setPriority(taskDTO.getPriority());
    // }
    // if (taskDTO.getStatus() != null) {
    // existingTask.setStatus(taskDTO.getStatus());
    // }

    // taskRepository.save(existingTask);
    // return "Updated";
    // }

    // public String deleteTask(long taskId) {
    // Task task = taskRepository.findById(taskId)
    // .orElseThrow(() -> new EntityNotFoundException("Task not found"));
    // task.getCreator().getCreatorTask().remove(task);
    // task.getAssignee().getAssigneTask().remove(task);
    // task.getProject().getTasks().remove(task);
    // taskRepository.delete(task);
    // return "Successsfully deleted";
    // }

    // public List<Task> findByDuedate() {
    // Date date = new Date();
    // List<Task> tasks = taskRepository.findByDueDateBefore(date);

    // if (tasks.size() == 0) {
    // throw new NoSuchElementException("No value is present in database , Please
    // change your request");
    // }

    // return tasks;
    // }
}
