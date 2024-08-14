package com._4dconcept.evaluation.repository;

import com._4dconcept.evaluation.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    Project findByStatus(String status);

    //List<Project> findByTagId(String id);
}