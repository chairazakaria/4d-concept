package com._4dconcept.evaluation.controller;

import com._4dconcept.evaluation.BusinessException;
import com._4dconcept.evaluation.Utils;
import com._4dconcept.evaluation.dto.DeveloperDTO;
import com._4dconcept.evaluation.entity.Developer;
import com._4dconcept.evaluation.service.DeveloperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("developers")
public class DeveloperController {

    static final Logger LOG = LoggerFactory.getLogger(DeveloperController.class);

    private DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    /**
     *
     * @param developerDTO
     * @return developer created
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public @ResponseBody Developer create(@RequestBody DeveloperDTO developerDTO) throws BusinessException {
        return this.developerService.createDeveloper(developerDTO);
    }

    /**
     *
     * @return a list of developers
     */
    @GetMapping()
    public List<DeveloperDTO> listDevelopers(@RequestParam(name = "all", required = false) String allDevelopers){
            if(! Utils.isStringNullOrEmptyOrBlank(allDevelopers) && !Boolean.parseBoolean(allDevelopers)){
                return this.developerService.getDevelopperAttached();
            }
            return this.developerService.getDeveloppers();
    }

    @GetMapping(value = "{id}")
    public DeveloperDTO getDeveloper(@PathVariable String id){
        return this.developerService.readDeveloper(id);
    }
}
