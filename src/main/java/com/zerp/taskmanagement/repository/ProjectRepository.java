package com.zerp.taskmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerp.taskmanagement.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project , Long>{

    List<Project> findByCreatorId(long id);

    boolean existsByIdAndCreatorId(Long id, long userId);
    

}
