package com._4dconcept.evaluation.mapper;

import com._4dconcept.evaluation.BusinessException;
import com._4dconcept.evaluation.Constants;
import com._4dconcept.evaluation.dto.DeveloperDTO;
import com._4dconcept.evaluation.entity.Developer;
import com._4dconcept.evaluation.entity.Project;
import com._4dconcept.evaluation.service.ProjectService;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DeveloperDTOMapper implements Function<Developer, DeveloperDTO> {

    private ProjectService projectService;

    public DeveloperDTOMapper(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public DeveloperDTO apply(Developer developer) {
        if (developer.getProject() == null) {
            return new DeveloperDTO(
                    developer.getId(), developer.getName(), null, null);
        }
        return new DeveloperDTO(
                developer.getId(), developer.getName(), developer.getProject().getId(), developer.getProject().getName()
        );
    }


    public Developer formatDeveloper(DeveloperDTO developerDTO) throws BusinessException {
        Project project = null;

        Developer developerToCreate = new Developer();
        developerToCreate.setName(developerDTO.getName());
        developerToCreate.setStatus(Constants.DEVELOPER_STATUS_ACTIVE);

        if (developerDTO.getProjectId() != null) {
            // Search project object by Id
            project =  this.projectService.getProjectById(developerDTO.getProjectId());
        }
        developerToCreate.setProject(project);

        return developerToCreate;
    }
}
