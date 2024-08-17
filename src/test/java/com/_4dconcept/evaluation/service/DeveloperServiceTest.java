package com._4dconcept.evaluation.service;

import com._4dconcept.evaluation.Constants;
import com._4dconcept.evaluation.dto.DeveloperDTO;
import com._4dconcept.evaluation.entity.Developer;
import com._4dconcept.evaluation.repository.DeveloperRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DeveloperServiceTest {

    @Autowired
    DeveloperRepository developerRepository;

    @Test
    public void shouldReturnAllDevelopers() {
        Developer developer1 = new Developer("1", "Zac", null, Constants.DEVELOPER_STATUS_ACTIVE);
        Developer developer2 = new Developer("2", "carol", null, Constants.DEVELOPER_STATUS_ACTIVE);
        this.developerRepository.saveAll(List.of(developer1, developer2));

        List<Developer> developers = this.developerRepository.findAll();

        Assertions.assertEquals((2), developers.size());
    }

    @Test
    public void shouldReturnDeveloperByName() {
        Developer developer1 = new Developer("1", "Zac", null, Constants.DEVELOPER_STATUS_ACTIVE);
        Developer developer2 = new Developer("2", "carol", null, Constants.DEVELOPER_STATUS_ACTIVE);
        this.developerRepository.saveAll(List.of(developer1, developer2));

        DeveloperDTO developerDTO = this.developerRepository.findByName("Zac");

        Assertions.assertEquals(developerDTO.getId(), developer1.getId());
        Assertions.assertEquals(developerDTO.getName(), developer1.getName());
    }
}