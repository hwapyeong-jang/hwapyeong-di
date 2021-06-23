package com.macaront.app.domain.entity;

import com.macaront.app.domain.dto.ChauffeurDto;

public class Chauffeur {

    private Long id;
    private final String name;
    private final String cellPhone;

    public Chauffeur(Long id, String name, String cellPhone) {
        this.id = id;
        this.name = name;
        this.cellPhone = cellPhone;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChauffeurDto convertToChauffeurDto() {
        ChauffeurDto dto = new ChauffeurDto();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setCellPhone(this.cellPhone);
        return dto;
    }

    public boolean isEmptyId() {
        return this.id == null;
    }

    public boolean isEqualsId(Long id) {
        return this.id.equals(id);
    }

    public boolean isEqualsValues(Chauffeur chauffeur) {
        return this.name.equals(chauffeur.name) && this.cellPhone.equals(chauffeur.cellPhone);
    }

}
