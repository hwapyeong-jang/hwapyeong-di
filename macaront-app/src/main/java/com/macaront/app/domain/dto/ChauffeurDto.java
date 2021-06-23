package com.macaront.app.domain.dto;

import com.macaront.app.api.param.ChauffeurParam;
import com.macaront.app.domain.entity.Chauffeur;

public class ChauffeurDto {

    private Long id;
    private String name;
    private String cellPhone;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getCellPhone() {
        return this.cellPhone;
    }

    public Chauffeur convertToChauffeur() {
        return new Chauffeur(this.id, this.name, this.cellPhone);
    }

    public boolean isEqualsId(ChauffeurDto dto) {
        return this.id.equals(dto.id);
    }

    public boolean isEqualsValues(ChauffeurDto dto) {
        return this.name.equals(dto.name) && this.cellPhone.equals(dto.cellPhone);
    }

    public boolean isEqualsValues(ChauffeurParam param) {
        return this.name.equals(param.getName()) && this.cellPhone.equals(param.getCellPhone());
    }

}
