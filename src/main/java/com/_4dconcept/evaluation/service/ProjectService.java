package com._4dconcept.evaluation.service;

import com._4dconcept.evaluation.BusinessException;
import com._4dconcept.evaluation.entity.Project;
import com._4dconcept.evaluation.entity.Projects;
import com._4dconcept.evaluation.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Value("${project.file.path}")
    private String projectFilePath;

    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository developerRepository) {
        this.projectRepository = developerRepository;
    }

    public List<Project> getProjects() {
        return this.projectRepository.findAll();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Projects loadProjectsFromXML(String projectFilePath) throws BusinessException {
        return ProjectsFileHelper.loadProjects(projectFilePath);
    }

    public List<Project> createProjects() throws BusinessException {
        List<Project> listProject = loadProjectsFromXML(projectFilePath).getProjects();
        return projectRepository.saveAll(listProject);
    }

    public Project getProjectById(String id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        return optionalProject.orElse(null);
    }

    public void deleteProjectById(String id) {
        projectRepository.deleteById(id);
    }

    /*public List<Project> searchByTag(String id) {
        return projectRepository.findByTagId(id);
    }*/

    public Project searchByStatus(String status) {
        return projectRepository.findByStatus(status);
    }
}
