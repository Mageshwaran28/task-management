package com.zerp.taskmanagement.dbrepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerp.taskmanagement.dbentity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project , Long>{

    List<Project> findByCreatorId(long id);
    

}
