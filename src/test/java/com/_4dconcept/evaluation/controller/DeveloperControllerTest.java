package com._4dconcept.evaluation.controller;

import com._4dconcept.evaluation.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DeveloperControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ProjectService projectService;

        @Test
        void shouldBeCreateDeveloper() throws Exception {

            createDeveloper("alice", "2");
            createDeveloper("bob", "2");
            createDeveloper("carol", "1");
            createDeveloper("dave", "1");
            createDeveloper("eve", "1");
            createDeveloper("franck");

            this.mockMvc.perform(get("/developers/list?all=true"))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.[*]", hasSize(6)))
                    .andExpect(jsonPath("$.[?(@.name == 'franck' && @.projectName == null)]").exists())
                    .andReturn();
        }

        private void createDeveloper(String name, String projectId) throws Exception {
            this.mockMvc.perform(post("/developers/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \""+ name +"\", \"projectId\": \""+ projectId +"\"}"))
                    .andExpect(status().isCreated());
        }
        private void createDeveloper(String name) throws Exception {
            this.mockMvc.perform(post("/developers/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \""+ name +"\"}"))
                    .andExpect(status().isCreated());
        }
    }