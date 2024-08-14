package com._4dconcept.evaluation.service;

import com._4dconcept.evaluation.BusinessException;
import com._4dconcept.evaluation.model.Developer;
import com._4dconcept.evaluation.model.Projects;
import com._4dconcept.evaluation.projects.ProjectsFileHelper;
import com._4dconcept.evaluation.repository.DeveloperRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeveloperService {

    private DeveloperRepository developerRepository;

    public DeveloperService(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    public List<Developer> getAllDevelopers(){
        return developerRepository.findAll();
    }

    public List<Developer> getDeveloperByProjectId(String projectId){
        return developerRepository.findByProjectId(projectId);
    }

    public Projects loadProjectsFromXML(String projectFilePath) throws BusinessException {
        return ProjectsFileHelper.loadProjects(projectFilePath);
    }

    public void create(Developer developerToCreate) {
        developerRepository.save(developerToCreate);
    }
}
