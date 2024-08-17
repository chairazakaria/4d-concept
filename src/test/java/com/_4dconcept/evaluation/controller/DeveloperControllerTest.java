package com._4dconcept.evaluation.controller;

import static org.hamcrest.Matchers.hasSize;
import com._4dconcept.evaluation.Constants;
import com._4dconcept.evaluation.dto.DeveloperDTO;
import com._4dconcept.evaluation.entity.Developer;
import com._4dconcept.evaluation.entity.Project;
import com._4dconcept.evaluation.entity.Projects;
import com._4dconcept.evaluation.mapper.DeveloperDTOMapper;
import com._4dconcept.evaluation.service.DeveloperService;
import com._4dconcept.evaluation.service.ProjectsFileHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DeveloperControllerTest {

    @Value("${project.file.path}")
    private String projectFilePath;

    private Projects projects;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeveloperService developerService;

    @Autowired
    private DeveloperDTOMapper developerDTOMapper;

    @BeforeEach
    void setUp() throws Exception {
        // Load Project from XML
        projects = ProjectsFileHelper.loadProjects(projectFilePath);
    }

    @Test
    void shouldBeReturnListofDeveloper() throws Exception {

        List<DeveloperDTO> developers = createDevelopers();
        when(this.developerService.getDeveloppers()).thenReturn(developers);

        this.mockMvc.perform(get("/developers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.[*]", hasSize(6)))
                .andExpect(jsonPath("$.[?(@.name == 'Zac' && @.projectName == 'First Project')]").exists())
                .andExpect(jsonPath("$.[?(@.name == 'alice' && @.projectName == 'Second Project')]").exists())
                .andExpect(jsonPath("$.[?(@.name == 'franck' && @.projectName == null)]").exists())
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void shouldCreateDeveloper() throws Exception {
        // Get First developer in the List for the exemple in this test
        DeveloperDTO developerDTO = createDevelopers().get(0);
        this.mockMvc.perform(post("/developers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + developerDTO.getName() + "\", \"projectId\": \"" + developerDTO.getProjectId() + "\"}"))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());;
    }

    @Test
    void shouldReturnDeveloper() throws Exception {
        // Get First developer in the List for Just for exemple in this test unit
        DeveloperDTO developerDTO = createDevelopers().get(0);
        when(this.developerService.readDeveloper(anyString())).thenReturn(developerDTO);

        this.mockMvc.perform(get("/developers/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id").value(developerDTO.getId()))
                .andExpect(jsonPath("$.name").value(developerDTO.getName()))
                .andExpect(jsonPath("$.projectId").value(developerDTO.getProjectId()))
                .andExpect(jsonPath("$.projectName").value(developerDTO.getProjectName()))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    public List<DeveloperDTO> createDevelopers() {
        // Load Project List from XML File
        Project project1 = ProjectsFileHelper.searchProjectFromXml(projects, "1");
        Project project2 = ProjectsFileHelper.searchProjectFromXml(projects, "2");

        // Create Developers DTO
        DeveloperDTO developerDTO1 = developerDTOMapper.apply(new Developer("1", "Zac", project1, Constants.DEVELOPER_STATUS_ACTIVE));
        DeveloperDTO developerDTO2 = developerDTOMapper.apply(new Developer("2", "carol", project1, Constants.DEVELOPER_STATUS_ACTIVE));
        DeveloperDTO developerDTO3 = developerDTOMapper.apply(new Developer("3", "alice", project2, Constants.DEVELOPER_STATUS_ACTIVE));
        DeveloperDTO developerDTO4 = developerDTOMapper.apply(new Developer("4", "bob", project2, Constants.DEVELOPER_STATUS_ACTIVE));
        DeveloperDTO developerDTO5 = developerDTOMapper.apply(new Developer("5", "eve", project1, Constants.DEVELOPER_STATUS_ACTIVE));
        DeveloperDTO developerDTO6 = developerDTOMapper.apply(new Developer("6", "franck", null, Constants.DEVELOPER_STATUS_ACTIVE));

        return List.of(developerDTO1, developerDTO2, developerDTO3, developerDTO4, developerDTO5, developerDTO6);
    }
}
