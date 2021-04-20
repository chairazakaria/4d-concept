package com._4dconcept.evaluation.controller;

import com._4dconcept.evaluation.Constants;
import com._4dconcept.evaluation.Utils;
import com._4dconcept.evaluation.model.Developer;
import com._4dconcept.evaluation.model.Project;
import com._4dconcept.evaluation.model.Projects;
import com._4dconcept.evaluation.repository.DeveloperRepository;
import com._4dconcept.evaluation.BusinessException;
import com._4dconcept.evaluation.projects.ProjectsFileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class RestApiController {

    static final Logger LOG = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    private DeveloperRepository DeveloperRepository;

    @Value("${project.file.path}")
    private String projectFilePath;

    /**
     *
     * @return
     */
    @PostMapping("developers/list")
    public ResponseEntity<?> listDevelopers(){
        try {
            List<Developer> developers = DeveloperRepository.findAll();

            ArrayList<DeveloperView> ds = new ArrayList<>();
            for(int i = 0; i < developers.size(); i++) {
                Developer developer = developers.get(i);

                DeveloperView d = new DeveloperView(developer.getId(), developer.getName());

                Projects projects = ProjectsFileHelper.loadProjects(projectFilePath);
                if(developer.getProjectId() != null) {
                    for (int j = 0; j < projects.getProjects().size(); j++) {
                        if (developer.getProjectId().equals(projects.getProjects().get(j).getId())) {
                            d.setProjectName(projects.getProjects().get(j).getName());
                        }
                    }
                }

                if (Constants.DEVELOPER_STATUS_ACTIVE.equals(developer.getStatus())) {
                    ds.add(d);
                } else {
                    LOG.error("The developer is inactive");
                }
            }

            return ResponseEntity.ok(ds);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(200).body("Error listing developers");
        }
    }

    /**
     *
     * @param developer - the developer to create
     * @return
     */
    @PostMapping("developers/create")
    public ResponseEntity ceateDevelopers(DeveloperView developer){

        try {
            Developer developerToCreate = new Developer();
            developerToCreate.setName(developer.getName());
            developerToCreate.setProjectId(developer.getProjectId());
            developerToCreate.setStatus(Constants.DEVELOPER_STATUS_ACTIVE);

            DeveloperRepository.save(developerToCreate);

           return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Failed to create");
        }
    }

    /**
     *
     * @return
     * @throws BusinessException
     */
    @PostMapping("projects")
    public ResponseEntity<ArrayList<ProjectView>> listProjects() throws BusinessException {
        Projects projects = ProjectsFileHelper.loadProjects(projectFilePath);
        ArrayList<ProjectView> projectViews = new ArrayList<>();

        for(Project project : projects.getProjects()){

            ProjectView v = new ProjectView();
            v.id = project.getId();
            v.name = project.getName();
            v.tags = project.getTags();
            List<Developer> developers = DeveloperRepository.findByProjectId(project.getId());

            // compute project status based on developers presence and status
            // no developer => ALL_INACTIVE
            // one developer active => SINGLE_ACTIVE
            // one developer inactive => SINGLE_INACTIVE
            // all developer active => NO_INACTIVE
            // all developer inactive => ALL_INACTIVE
            // more than 50% of inactive developer => MAJORITY_INACTIVE
            // at least one inactive developer => AT_LEAST_ONE_INACTIVE
            if (developers.size() == 0){
                v.status = Constants.PROJECT_STATUS_ALL_INACTIVE;
            } else if (developers.size() == 1) {
                if(developers.get(0).getStatus() == Constants.DEVELOPER_STATUS_ACTIVE){
                    v.status = Constants.PROJECT_STATUS_SINGLE_ACTIVE;
                } else {
                    v.status = Constants.PROJECT_STATUS_SINGLE_INACTIVE;
                }
            } else {
                int countInactive = 0;
                int countTotal = 0;

                for(Developer developer : developers){
                    if(developer.getStatus().equals(Constants.DEVELOPER_STATUS_INACTIVE)){
                        countInactive ++;
                    }
                    countTotal ++;
                }

                int halfTotal = countTotal / 2;

                if(countInactive == countTotal) {
                    v.status = Constants.PROJECT_STATUS_ALL_INACTIVE;
                } else if (countInactive >= halfTotal){
                    v.status = Constants.PROJECT_STATUS_MAJORITY_INACTIVE;
                } else if (countInactive > 0){
                    v.status = Constants.PROJECT_STATUS_AT_LEAST_ONE_INACTIVE;
                }else if (countInactive == 0){
                    v.status = Constants.PROJECT_STATUS_NO_INACTIVE;
                }
            }
            projectViews.add(v);
        }

        return ResponseEntity.ok(projectViews);
    }

    /**
     *
     * @return
     * @throws BusinessException
     */
    @PostMapping("projects/tag/{tag}")
    public ResponseEntity<ArrayList<ProjectView>> listProjectsByStatusAndTag(@RequestParam(name = "status", required = true) String status, @PathVariable("tag") String tag) throws BusinessException {

        Projects projects = ProjectsFileHelper.loadProjects(projectFilePath);
        ArrayList<ProjectView> projectViews = new ArrayList<>();
        for(Project project : projects.getProjects()){

            ProjectView v = new ProjectView();
            v.id = project.getId();
            v.name = project.getName();
            v.tags = project.getTags();
            List<Developer> developers = DeveloperRepository.findByProjectId(project.getId());

            // compute project status based and filter
            if (developers.size() == 0 && status.equals(Constants.PROJECT_STATUS_ALL_INACTIVE)){
                v.status = Constants.PROJECT_STATUS_ALL_INACTIVE;
                projectViews.add(v);
            } else if (developers.size() == 1) {
                if(developers.get(0).getStatus().equals(Constants.DEVELOPER_STATUS_ACTIVE) && status.equals(Constants.PROJECT_STATUS_SINGLE_ACTIVE)){
                    v.status = Constants.PROJECT_STATUS_SINGLE_ACTIVE;
                    projectViews.add(v);
                } else {
                    v.status = Constants.PROJECT_STATUS_SINGLE_INACTIVE;
                    projectViews.add(v);
                }
            } else {
                int countInactive = 0;
                int countTotal = 0;

                for(Developer developer : developers){
                    if(developer.getStatus().equals(Constants.DEVELOPER_STATUS_INACTIVE)){
                        countInactive ++;
                    }
                    countTotal ++;
                }

                int halfTotal = countTotal / 2;

                if(countInactive == countTotal && status.equals(Constants.PROJECT_STATUS_ALL_INACTIVE)) {
                    v.status = Constants.PROJECT_STATUS_ALL_INACTIVE;
                    projectViews.add(v);
                } else if (countInactive >= halfTotal && status.equals(Constants.PROJECT_STATUS_MAJORITY_INACTIVE)){
                    v.status = Constants.PROJECT_STATUS_MAJORITY_INACTIVE;
                    projectViews.add(v);
                } else if (countInactive > 0 && status.equals(Constants.PROJECT_STATUS_AT_LEAST_ONE_INACTIVE)){
                    v.status = Constants.PROJECT_STATUS_AT_LEAST_ONE_INACTIVE;
                    projectViews.add(v);
                }else if (countInactive == 0 && status.equals(Constants.PROJECT_STATUS_NO_INACTIVE)){
                    v.status = Constants.PROJECT_STATUS_NO_INACTIVE;
                    projectViews.add(v);
                }
            }

            // filter by tags
            if(!Utils.isListNullOrEmpty(project.getTags())) {
                boolean matchTag = false;
                for (String projectTag : project.getTags()) {
                    if (projectTag.equalsIgnoreCase(tag)){
                        matchTag = true;
                        break;
                    }
                }
                if(!matchTag){
                    projectViews.remove(v);
                }

            }
        }

        return ResponseEntity.ok(projectViews);
    }


    /**
     *
     * @param id - the id
     * @return
     */
    @PostMapping("developers/{id}/desactivate")
    public ResponseEntity<Developer> deleteDeveloper(@PathVariable(name= "id", required = true) String id){
        Developer one = DeveloperRepository.getOne(id);
        one.setStatus(Constants.DEVELOPER_STATUS_INACTIVE);

        DeveloperRepository.save(one);
        return ResponseEntity.ok().build();
    }

}
