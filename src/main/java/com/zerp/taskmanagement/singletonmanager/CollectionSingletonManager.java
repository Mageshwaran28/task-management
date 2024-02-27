package com.zerp.taskmanagement.singletonmanager;

import java.util.HashSet;
import java.util.LinkedList;

import org.springframework.stereotype.Service;

import com.zerp.taskmanagement.model.Project;
import com.zerp.taskmanagement.model.Task;
import com.zerp.taskmanagement.model.User;

@Service
public class CollectionSingletonManager {

    private LinkedList<Task> linkedListTaskInstance;
    private LinkedList<Project> linkedListProjectInstance;
    private HashSet<User> hashSetUserInstance;

    public CollectionSingletonManager() {
        this.linkedListTaskInstance = new LinkedList<Task>();
        this.linkedListProjectInstance = new LinkedList<Project>();
        this.hashSetUserInstance = new HashSet<User>();
    }

    public LinkedList<Task> getTaskLinkedListInstance() {
        return linkedListTaskInstance;
    }

    public LinkedList<Project> getProjectLinkedListInstance() {
        return linkedListProjectInstance;
    }

    public HashSet<User> getUserHashSetInstance() {
        return hashSetUserInstance;
    }

}
