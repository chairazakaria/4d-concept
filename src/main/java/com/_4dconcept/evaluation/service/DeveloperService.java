package com._4dconcept.evaluation.service;

import com._4dconcept.evaluation.BusinessException;
import com._4dconcept.evaluation.Constants;
import com._4dconcept.evaluation.dto.DeveloperDTO;
import com._4dconcept.evaluation.entity.Developer;
import com._4dconcept.evaluation.entity.Project;
import com._4dconcept.evaluation.entity.Projects;
import com._4dconcept.evaluation.mapper.DeveloperDTOMapper;
import com._4dconcept.evaluation.repository.DeveloperRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class DeveloperService {

    private DeveloperDTOMapper developerDTOMapper;
    private DeveloperRepository developerRepository;


    public DeveloperService(DeveloperDTOMapper developerDTOMapper, DeveloperRepository developerRepository) {
        this.developerDTOMapper = developerDTOMapper;
        this.developerRepository = developerRepository;
    }

    public Developer createDeveloper(DeveloperDTO developerDTO) throws BusinessException {
        Developer developer =  developerDTOMapper.createDeveloper(developerDTO);
        return developerRepository.save(developer);
    }

    public Stream<DeveloperDTO> getDeveloppers() {
        return this.developerRepository.findAll().stream().map(developerDTOMapper);
    }

    public Stream<DeveloperDTO> getDevelopperAttached() {
        return this.developerRepository.findByProjectNotNull().stream().map(developerDTOMapper);
    }
}
