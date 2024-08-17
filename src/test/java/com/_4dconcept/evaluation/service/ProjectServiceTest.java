package com._4dconcept.evaluation.service;

import com._4dconcept.evaluation.Constants;
import com._4dconcept.evaluation.entity.Project;
import com._4dconcept.evaluation.repository.ProjectRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProjectServiceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Test
    public void shouldReturnAllProjects() {
        Project project1 = new Project("1", "First Project");
        Project project2 = new Project("2", "Second Project");
        this.projectRepository.saveAll(List.of(project1, project2));

        List<Project> projects = this.projectRepository.findAll();

        Assertions.assertEquals((2), projects.size());
    }

    @Test
    public void shouldReturnProjectByStatus() {
        Project project1 = new Project("1", "First Project", null, null, Constants.PROJECT_STATUS_SINGLE_ACTIVE);
        Project project2 = new Project("2", "Second Project", null, null, Constants.PROJECT_STATUS_SINGLE_INACTIVE);
        Project project3 = new Project("3", "Third Project");
        this.projectRepository.saveAll(List.of(project1, project2, project3));

        Project  project  = this.projectRepository.findByStatus("SINGLE_ACTIVE");

        Assertions.assertEquals(project1.getId(), project.getId());
        Assertions.assertEquals(project1.getName(), project.getName());
        Assertions.assertEquals(project1.getStatus(), project.getStatus());
    }
}