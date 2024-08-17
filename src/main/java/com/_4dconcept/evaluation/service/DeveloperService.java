package com._4dconcept.evaluation.service;

import com._4dconcept.evaluation.BusinessException;
import com._4dconcept.evaluation.dto.DeveloperDTO;
import com._4dconcept.evaluation.entity.Developer;
import com._4dconcept.evaluation.mapper.DeveloperDTOMapper;
import com._4dconcept.evaluation.repository.DeveloperRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeveloperService {

    private DeveloperDTOMapper developerDTOMapper;
    private DeveloperRepository developerRepository;


    public DeveloperService(DeveloperDTOMapper developerDTOMapper, DeveloperRepository developerRepository) {
        this.developerDTOMapper = developerDTOMapper;
        this.developerRepository = developerRepository;
    }

    public Developer createDeveloper(DeveloperDTO developerDTO) throws BusinessException {
        Developer developer = developerDTOMapper.formatDeveloper(developerDTO);
        return developerRepository.save(developer);
    }

    public List<DeveloperDTO> getDeveloppers() {
        return this.developerRepository.findAll().stream().map(developerDTOMapper).collect(Collectors.toList());
    }

    public List<DeveloperDTO> getDevelopperAttached() {
        return (List<DeveloperDTO>) this.developerRepository.findByProjectNotNull().stream().map(developerDTOMapper).collect(Collectors.toList());
    }

    public DeveloperDTO readDeveloper(String id) {
        Optional<Developer> optionalDeveloper = developerRepository.findById(id);
        Developer developer = optionalDeveloper.orElse(null);
        return developer != null ? developerDTOMapper.apply(developer) : null;
    }
}
