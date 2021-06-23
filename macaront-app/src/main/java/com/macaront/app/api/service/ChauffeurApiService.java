package com.macaront.app.api.service;

import com.macaront.app.api.param.ChauffeurParam;
import com.macaront.app.domain.dto.ChauffeurDto;
import com.macaront.app.domain.service.ChauffeurService;
import com.macaront.framework.web.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ChauffeurApiService {

    private static final Logger logger = LoggerFactory.getLogger(ChauffeurApiService.class);

    private final ChauffeurService chauffeurService;

    private ChauffeurApiService(ChauffeurService chauffeurService) {
        this.chauffeurService = chauffeurService;
    }

    public List<ChauffeurDto> getList() {
        logger.info("Chauffeur API Service Layer - Get Chauffeur List");
        return chauffeurService.getList();
    }

    public ChauffeurDto getData(Long id) {
        logger.info("Chauffeur API Service Layer - Get Chauffeur Data, id: {}", id);
        return chauffeurService.getData(id);
    }

    public ChauffeurDto add(ChauffeurParam param) {
        logger.info("Chauffeur API Service Layer - Add Chauffeur, name: {}, cellPhone: {}",
                param.getName(), param.getCellPhone()
        );
        param.validateForAdd();
        return chauffeurService.save(param.convertToChauffeurDto());
    }

    public ChauffeurDto modify(Long id, ChauffeurParam param) {
        logger.info("Chauffeur API Service Layer - Modify Chauffeur, id: {}, name: {}, cellPhone: {}",
                id, param.getName(), param.getCellPhone()
        );
        param.validateForModify(id);
        ChauffeurDto dto = chauffeurService.getData(id);
        param.updateToChauffeurDto(dto);
        return chauffeurService.save(dto);
    }

    public void delete(Long id) {
        logger.info("Chauffeur API Service Layer - Delete Chauffeur, id: {}", id);
        chauffeurService.delete(id);
    }

    public static class Mock extends ChauffeurApiService {

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
            return this.chauffeurDto;
        }

        @Override
        public ChauffeurDto add(ChauffeurParam param) {
            return this.chauffeurDto;
        }

        @Override
        public ChauffeurDto modify(Long id, ChauffeurParam param) {
            return this.chauffeurDto;
        }

        @Override
        public void delete(Long id) {}

    }

}
