package com.zerp.taskmanagement.service;

import java.io.File;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.model.Project;
import com.zerp.taskmanagement.model.Task;
import com.zerp.taskmanagement.model.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private void sendMail(String to, String subject, String message) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(message, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        javaMailSender.send(mimeMessage);
    }

    private void sendMailWithAttachment(File file, String to, String subject, String message) {

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(message, true);
            messageHelper.addAttachment(file.getName(), file);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String acknwoledgeForCreatedTask(Task task) {
        StringBuilder creatorMessage = new StringBuilder();
        StringBuilder assigneeMessage = new StringBuilder();

        String creatorSubject = "Task Successfully Created and Assigned - " + task.getName();
        String assigneeSubject = "Assignment of Task - " + task.getName();

        creatorMessage.append("Dear " + task.getCreator().getName() + "<br>");
        creatorMessage.append(
                "I'm pleased to inform you that a new task has been successfully created and assigned in our project, <strong>"
                        + task.getProject().getName() + "</strong>. The task details are as follows:<br>");
        creatorMessage.append(getTaskDetails(task) + "<br>");
        creatorMessage.append("Additionally, the task has been assigned to the following team members:<br>");
        creatorMessage.append("<strong>Assignees: </strong><br>");
        creatorMessage.append(getAssignees(task.getAssignees()) + "<br>");
        creatorMessage.append("Thank you for your contribution to our project.");

        sendMail(task.getCreator().getEmail(), creatorSubject, creatorMessage.toString());

        assigneeMessage.append("I am pleased to inform you that a new task has been assigned to you by "
                + task.getCreator().getEmail() + " as part of our ongoing project," + task.getProject().getName()
                + ". You will be working alongside the following co-assignees:<br>");
        assigneeMessage.append(getAssignees(task.getAssignees()) + "<br>");
        assigneeMessage.append(getTaskDetails(task) + "<br>");
        assigneeMessage.append("Thank you for your continued dedication to our project.");

        sendMailForAssignees(task.getAssignees(), assigneeSubject, assigneeMessage.toString());
        return "Email sent successfully";
    }

    private StringBuilder getAssignees(Set<User> assignees) {
        StringBuilder listOfAssignees = new StringBuilder();
        for (User user : assignees) {
            listOfAssignees.append(" - " + user.getName() + " ( " + user.getEmail() + " ) " + "<br>");
        }

        return listOfAssignees;
    }

    private StringBuilder getTaskDetails(Task task) {
        StringBuilder taskDetails = new StringBuilder();
        taskDetails.append("<strong>Task Details: </strong><br>");
        taskDetails.append("<li><strong>Task Name: </strong>").append(task.getName() + "</li>");
        taskDetails.append("<li><strong>Description: </strong>").append(task.getDescription() + "</li>");
        taskDetails.append("<li><strong>Priority: </strong>").append(task.getPriority() + "</li>");
        taskDetails.append("<li><strong>Status: </strong>").append(task.getStatus() + "</li>");
        taskDetails.append("<li><strong>Start Date: </strong>").append(task.getStartDate() + "</li>");
        taskDetails.append("<li><strong>Due Date: </strong>").append(task.getDueDate() + "</li>");

        return taskDetails;
    }

    private void sendMailForAssignees(Set<User> assignees, String subject, String text) {
        for (User user : assignees) {
            String assignee = "Dear " + user.getName() + "<br>";
            sendMail(user.getEmail(), subject, assignee + text);
        }
    }

    public String acknwoledgeForCreatedProject(Project project) {
        StringBuilder creatorMessage = new StringBuilder();
        StringBuilder assigneeMessage = new StringBuilder();

        String creatorSubject = "Project Created and Assigned Successfully - " + project.getName();
        String assigneeSubject = "You Have Been Assigned to Project - " + project.getName();

        creatorMessage.append("Dear " + project.getCreator().getName() + "<br>");
        creatorMessage.append("I am pleased to inform you that a new project, <strong>" + project.getName()
                + "</strong>, has been created and assigned successfully.<br>");
        
        creatorMessage.append(getProjectDetails(project)+"<br>");
        creatorMessage.append("Additionally, the following team members have been assigned to work on this project:<br>");
        creatorMessage.append(getAssignees(project.getAssignees()));
        creatorMessage.append("Thank you for your continued contributions to our organization.");
        sendMail(project.getCreator().getEmail(), creatorSubject, creatorMessage.toString());

        assigneeMessage.append("I am writing to inform you that you have been assigned to work on the project: <strong>"+project.getName()+"</strong>, created by <strong>"+project.getCreator().getName()+"</strong>.");
        assigneeMessage.append(getProjectDetails(project));
        assigneeMessage.append("You will be collaborating with the following team members on this project:<br>");
        assigneeMessage.append(getAssignees(project.getAssignees()));
        assigneeMessage.append("Thank you for your commitment and dedication to our organization.");

        sendMailForAssignees(project.getAssignees(), assigneeSubject, assigneeMessage.toString());
        return "Email sent successfully";
    }

    private StringBuilder getProjectDetails(Project project) {
        StringBuilder projectDetails = new StringBuilder();
        projectDetails.append("<strong>Project Details: </strong><br>");
        projectDetails.append("<li><strong>Task Name: </strong>").append(project.getName() + "</li>");
        projectDetails.append("<li><strong>Description: </strong>").append(project.getDescription() + "</li>");
        projectDetails.append("<li><strong>Status: </strong>").append(project.getStatus() + "</li>");
        projectDetails.append("<li><strong>Start Date: </strong>").append(project.getStartDate() + "</li>");
        projectDetails.append("<li><strong>Due Date: </strong>").append(project.getDueDate() + "</li>");
        return projectDetails;
    }

}
