package com._4dconcept.evaluation.controller;

import com._4dconcept.evaluation.BusinessException;
import com._4dconcept.evaluation.Constants;
import com._4dconcept.evaluation.entity.Developer;
import com._4dconcept.evaluation.entity.Project;
import com._4dconcept.evaluation.service.DeveloperService;
import com._4dconcept.evaluation.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("projects")
public class ProjectController {

    static final Logger LOG = LoggerFactory.getLogger(DeveloperController.class);

    private ProjectService projectService;


    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * @param project
     * @return the project created
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public Project create(@RequestBody Project project) {
        project.setStatus(Constants.PROJECT_STATUS_SINGLE_ACTIVE);
        return this.projectService.createProject(project);
    }

    /**
     * @return a list of project
     */
    @GetMapping()
    public List<Project> ListProjects() {
        return this.projectService.getProjects();
    }

    /**
     * Function type void => load projects from XML and created
     *
     * @throws BusinessException
     */
    @GetMapping(value = "load")
    public void LoadAndSaveProjects() throws BusinessException {
        this.projectService.createProjects();
    }

    /**
     * @return a project by Id
     */
    @GetMapping(path = "{id}")
    public Project getProject(@PathVariable String id) {
        return this.projectService.getProjectById(id);
    }

    /**
     * @return a project by Id
     */
    @DeleteMapping(path = "{id}")
    public void deleteProject(@PathVariable String id) {
        this.projectService.deleteProjectById(id);
    }

    /**
     * @return a list project by tag Id
     */
    @GetMapping(path = "{status}")
    public Project searchProjectByStatus(@PathVariable String status) {
        return this.projectService.searchByStatus(status);
    }

    /**
     *
     * @return a list project by tag Id
     */
    /*@GetMapping(path = "tag/{id}")
    public List<Project> searchProjectByTag(@PathVariable String id){
        return this.projectService.searchByTag(id);
    }*/
}
