package com.macaront.app.domain.service;

import com.macaront.app.domain.dto.ChauffeurDto;
import com.macaront.app.domain.repository.ChauffeurRepository;
import com.macaront.app.domain.entity.Chauffeur;
import com.macaront.framework.web.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChauffeurService {

    private static final Logger logger = LoggerFactory.getLogger(ChauffeurService.class);

    private final ChauffeurRepository chauffeurRepository;

    private ChauffeurService(ChauffeurRepository chauffeurRepository) {
        this.chauffeurRepository = chauffeurRepository;
    }

    public List<ChauffeurDto> getList() {
        logger.info("Chauffeur Domain Service Layer - Get Chauffeur List");
        return chauffeurRepository.findAll().stream()
                .map(Chauffeur::convertToChauffeurDto)
                .collect(Collectors.toList());
    }

    public ChauffeurDto getData(Long id) {
        logger.info("Chauffeur Domain Service Layer - Get Chauffeur Data, ID: {}", id);
        Optional<ChauffeurDto> chauffeurDto = chauffeurRepository.findById(id)
                .map(Chauffeur::convertToChauffeurDto);
        if (!chauffeurDto.isPresent()) {
            logger.error("Not Exist Chauffeur");
        }
        return chauffeurDto.orElse(null);
    }

    public ChauffeurDto save(ChauffeurDto dto) {
        logger.info("Chauffeur Domain Service Layer - Add/Modify Chauffeur, DTO: {}", dto);
        return chauffeurRepository.save(dto.convertToChauffeur()).convertToChauffeurDto();
    }

    public void delete(Long id) {
        logger.info("Chauffeur Domain Service Layer - Delete Chauffeur, ID: {}", id);
        chauffeurRepository.delete(id);
    }

    public static class Mock extends ChauffeurService {

        private Mock() {
            super(null);
        }

        public List<ChauffeurDto> chauffeurDtoList = null;

        public ChauffeurDto chauffeurDto = null;

        @Override
        public List<ChauffeurDto> getList() {
            return this.chauffeurDtoList;
        }

        @Override
        public ChauffeurDto getData(Long id) {
            return chauffeurDto;
        }

        @Override
        public ChauffeurDto save(ChauffeurDto dto) {
            return dto;
        }

        @Override
        public void delete(Long id) {}

    }
}
