package com._4dconcept.evaluation.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RestApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void completeScenario() throws Exception {
        this.mockMvc.perform(post("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(8)));

        this.mockMvc.perform(post("/api/projects/tag/internal?status=NO_INACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(5)));

        createDeveloper("alice", "2");
        createDeveloper("bob", "2");
        createDeveloper("carol", "3");
        createDeveloper("dave", "3");
        createDeveloper("eve", "3");

        MvcResult result = this.mockMvc.perform(post("/api/developers/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(5)))
                .andReturn();

        List<Integer> developerIds = JsonPath.read(result.getResponse().getContentAsString(), "$.[*].id");

        this.mockMvc.perform(post("/api/developers/"+developerIds.get(0)+"/desactivate"))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/api/developers/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(4)));
    }

    private void createDeveloper(String name, String projectId) throws Exception {
        this.mockMvc.perform(post("/api/developers/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \""+ name +"\", \"projectId\": \""+ projectId +"\"}"))
                .andExpect(status().isCreated());
    }

}