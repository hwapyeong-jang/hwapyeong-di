package com.macaront.app.api.param;

import com.macaront.app.domain.dto.ChauffeurDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChauffeurParam {

    private static final Logger logger = LoggerFactory.getLogger(ChauffeurParam.class);

    private String name;
    private String cellPhone;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellPhone() {
        return this.cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public void validateForAdd() {
        commonValidate();
    }

    public void validateForModify(Long id) {
        if (id == null) {
            logger.error("ID가 비어 있습니다.");
        }
        commonValidate();
    }

    private void commonValidate() {
        if (isEmptyName()) {
            logger.error("이름이 비어 있습니다.");
        }

        if (isEmptyCellPhone()) {
            logger.error("휴대폰 번호가 비어 있습니다.");
        }
    }

    private boolean isEmptyName() {
        return this.name == null || this.name.trim().isEmpty();
    }

    private boolean isEmptyCellPhone() {
        return this.cellPhone == null || this.cellPhone.trim().isEmpty();
    }

    public ChauffeurDto convertToChauffeurDto() {
        ChauffeurDto dto = new ChauffeurDto();
        dto.setName(this.name);
        dto.setCellPhone(this.cellPhone);
        return dto;
    }

    public void updateToChauffeurDto(ChauffeurDto dto) {
        dto.setName(this.name);
        dto.setCellPhone(this.cellPhone);
    }

}
