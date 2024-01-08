package com.zerp.taskmanagement.taskservice;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.customexception.InvalidInputException;
import com.zerp.taskmanagement.dbentity.Project;
import com.zerp.taskmanagement.dbentity.Task;
import com.zerp.taskmanagement.dbentity.User;
import com.zerp.taskmanagement.dbrepository.ProjectRepository;
import com.zerp.taskmanagement.dbrepository.TaskRepository;
import com.zerp.taskmanagement.dbrepository.UserRepository;
import com.zerp.taskmanagement.dto.TaskDTO;
import com.zerp.taskmanagement.myenum.Priority;
import com.zerp.taskmanagement.myenum.Status;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    public List<Task> findAll() {

        List<Task> tasks = taskRepository.findAll();

        if (tasks.size() == 0) {
            throw new NoSuchElementException("No value is present in database , Please change your request");
        }

        return tasks;
    }

    public String addTask(TaskDTO taskDTO) {
        Task task = new Task();
        if (validateTask(taskDTO)) {
            throw new InvalidInputException("601", "Inavalid input task , please check your input");
        } else if (validateCreatorAndAssignee(taskDTO)) {
            throw new InvalidInputException("601", "Invalid creator or assignee input , please check your input");
        } else if (validateProject(taskDTO)) {
            throw new InvalidInputException("601", "Invalid project input , please check your input");
        }

        task.setTaskId(taskDTO.getTaskId());
        task.setTaskTitle(taskDTO.getTaskTitle());
        task.setTaskDescription(taskDTO.getTaskDescription());
        task.setPriority(taskDTO.getPriority());
        task.setDueDate(taskDTO.getDueDate());
        task.setStatus(taskDTO.getStatus());
        createTask(task, taskDTO);
        taskRepository.save(task);
        return "success";
    }

    private Task createTask(Task task, TaskDTO taskDTO) {
        User creator = userRepository.findById(taskDTO.getCreatorId()).get();
        User assignee = userRepository.findById(taskDTO.getAssigneeId()).get();
        Project project = projectRepository.findById(taskDTO.getProjectId()).get();
        task.setProject(project);
        task.setCreator(creator);
        task.setAssignee(assignee);
        return task;
    }

    private boolean validateProject(TaskDTO taskDTO) {
        Project project = projectRepository.findByProjectId(taskDTO.getProjectId());
        if (project == null) {
            return true;
        }

        return false;
    }

    private boolean validateCreatorAndAssignee(TaskDTO taskDTO) {
        User creator = userRepository.findByUserId(taskDTO.getCreatorId());
        User assignee = userRepository.findByUserId(taskDTO.getAssigneeId());
        if (creator == null || assignee == null) {
            return true;
        } else if (taskDTO.getCreatorId() == taskDTO.getAssigneeId()) {
            return true;
        }

        return false;
    }

    private boolean validateTask(TaskDTO taskDTO) {

        if (taskDTO.getTaskTitle().isEmpty() ||
                taskDTO.getTaskTitle().length() == 0 ||
                taskDTO.getTaskDescription().isEmpty() ||
                taskDTO.getTaskDescription().length() == 0 ||
                taskDTO.getDueDate().isEmpty()) {
            return true;
        }
        return false;
    }

    public Task findByTaskId(long taskId) {
        Task task = taskRepository.findByTaskId(taskId);
        if (task == null) {
            throw new NoSuchElementException("No value is present in database , Please change your request");
        }
        return task;
    }

    public List<Task> findByStatus(Status status) {
        List<Task> task = taskRepository.findByStatus(status);
        if (task.size() == 0) {
            throw new NoSuchElementException("No value is present in database , Please change your request");
        }
        return task;
    }

    public List<Task> findByPriority(Priority priority) {
        List<Task> task = taskRepository.findByPriority(priority);
        if (task.size() == 0) {
            throw new NoSuchElementException("No value is present in database , Please change your request");
        }
        return task;
    }

    public String updateTask(long taskId, TaskDTO taskDTO) {

        Task existingTask = taskRepository.findByTaskId(taskId);

        if (taskDTO.getCreatorId() != 0 || taskDTO.getAssigneeId() != 0) {
            if (validateCreatorAndAssignee(taskDTO)) {
                throw new InvalidInputException("601", "Invalid creator or assignee input , please check your input");
            }
        } else if (taskDTO.getProjectId() != 0) {
            if (validateProject(taskDTO)) {
                throw new InvalidInputException("601", "Invalid project input , please check your input");
            }
        }

        if (taskDTO.getTaskTitle() != null && taskDTO.getTaskTitle().length() != 0)
            existingTask.setTaskTitle(taskDTO.getTaskTitle());

        if (taskDTO.getTaskDescription() != null && taskDTO.getTaskDescription().length() != 0)
            existingTask.setTaskDescription(taskDTO.getTaskDescription());

        if (taskDTO.getPriority() != null)
            existingTask.setPriority(taskDTO.getPriority());

        if (taskDTO.getDueDate() != null && taskDTO.getDueDate().length() != 0)
            existingTask.setDueDate(taskDTO.getDueDate());

        if (taskDTO.getStatus() != null)
            existingTask.setStatus(taskDTO.getStatus());

        if (taskDTO.getCreatorId() != 0) {
            User creator = userRepository.findById(taskDTO.getCreatorId()).get();
            existingTask.setCreator(creator);
        }
        if (taskDTO.getAssigneeId() != 0) {
            User assignee = userRepository.findById(taskDTO.getAssigneeId()).get();
            existingTask.setAssignee(assignee);
        }
        if (taskDTO.getProjectId() != 0) {
            Project project = projectRepository.findById(taskDTO.getProjectId()).get();
            existingTask.setProject(project);
        }

        taskRepository.save(existingTask);
        return "Updated";
    }

    public String deleteTask(long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.getCreator().getCreatorTask().remove(task);
        task.getAssignee().getAssigneTask().remove(task);
        task.getProject().getTasks().remove(task);
        taskRepository.delete(task);
        return "Successsfully deleted";
    }
}
