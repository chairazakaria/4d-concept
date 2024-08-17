package com._4dconcept.evaluation.controller;

import com._4dconcept.evaluation.entity.Project;
import com._4dconcept.evaluation.entity.Projects;
import com._4dconcept.evaluation.service.ProjectService;
import com._4dconcept.evaluation.service.ProjectsFileHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Value("${project.file.path}")
    private String projectFilePath;

    private Projects projects;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @BeforeEach
    void setUp() throws Exception {
        // Load Project from XML
        projects = ProjectsFileHelper.loadProjects(projectFilePath);
    }

    @Test
    void shouldLoadProjects() throws Exception {

        when(this.projectService.createProjects()).thenReturn(projects.getProjects());

        this.mockMvc.perform(get("/projects/load"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.id == '1' && @.name == 'First Project')]").exists())
                .andExpect(jsonPath("$.[?(@.id == '2' && @.name == 'Second Project')]").exists());
    }

    @Test
    void shouldReturnListOfProjects() throws Exception {
        Project project1 = new Project("1", "First Project");
        Project project2 = new Project("2", "Second Project");

        when(this.projectService.getProjects()).thenReturn(List.of(project1, project2));

        this.mockMvc.perform(get("/projects"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.id == '1' && @.name == 'First Project')]").exists())
                .andExpect(jsonPath("$.[?(@.id == '2' && @.name == 'Second Project')]").exists());
    }

}