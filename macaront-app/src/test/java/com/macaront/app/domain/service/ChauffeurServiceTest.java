package com.macaront.app.domain.service;

import com.macaront.app.domain.dto.ChauffeurDto;
import com.macaront.app.domain.repository.ChauffeurRepository;
import com.macaront.app.domain.entity.Chauffeur;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ChauffeurServiceTest {

    private static ChauffeurService chauffeurService;

    private static ChauffeurRepository.Mock chauffeurRepositoryMock;

    @BeforeAll
    public static void injectMock() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ChauffeurRepository.Mock> repositoryMockConstructor =
                ChauffeurRepository.Mock.class.getDeclaredConstructor();
        repositoryMockConstructor.setAccessible(true);
        chauffeurRepositoryMock = repositoryMockConstructor.newInstance();

        Constructor<ChauffeurService> serviceConstructor = ChauffeurService.class.getDeclaredConstructor(ChauffeurRepository.class);
        serviceConstructor.setAccessible(true);
        chauffeurService = serviceConstructor.newInstance(chauffeurRepositoryMock);
    }

    @Test
    public void testGetList_exist_case() {

        // given
        chauffeurRepositoryMock.chauffeurList = getChauffeurList();

        // when - then
        testGetList();
    }

    @Test
    public void testGetList_not_exist_case() {

        // given
        chauffeurRepositoryMock.chauffeurList = new ArrayList<>();

        // when - then
        testGetList();

    }

    private void testGetList() {

        // when
        List<ChauffeurDto> resList = chauffeurService.getList();

        // then
        Assertions.assertEquals(chauffeurRepositoryMock.chauffeurList.size(), resList.size());

    }

    @Test
    public void testGetData_exist_case() {

        // given
        Long givenId = 1L;
        chauffeurRepositoryMock.chauffeur = getChauffeur();

        // when
        ChauffeurDto resChauffeurDto = chauffeurService.getData(givenId);

        // when
        Assertions.assertEquals(givenId, resChauffeurDto.getId());

    }

    @Test
    public void testGetData_not_exist_case() {

        // given
        Long givenId = 1L;
        chauffeurRepositoryMock.chauffeur = null;

        // when
        ChauffeurDto resChauffeurDto = chauffeurService.getData(givenId);

        // when
        Assertions.assertNull(resChauffeurDto);

    }

    @Test
    public void testSave() {

        // given
        ChauffeurDto givenChauffeurDto = generateChauffeurDtoForSave();
        chauffeurRepositoryMock.chauffeur = givenChauffeurDto.convertToChauffeur();

        // when
        ChauffeurDto resChauffeurDto = chauffeurService.save(givenChauffeurDto);

        // then
        Assertions.assertTrue(givenChauffeurDto.isEqualsId(resChauffeurDto));
        Assertions.assertTrue(givenChauffeurDto.isEqualsValues(resChauffeurDto));

    }

    @Test
    public void testDelete() {

        // given
        Long givenId = 1L;

        // when
        chauffeurService.delete(givenId);

    }

    private List<Chauffeur> getChauffeurList() {
        List<Chauffeur> list = new ArrayList<>();
        list.add(getChauffeur());
        return list;
    }

    private Chauffeur getChauffeur() {
        return new Chauffeur(1L, "장화평", "010-4104-7721");
    }

    private ChauffeurDto generateChauffeurDtoForSave() {
        ChauffeurDto chauffeurDto = new ChauffeurDto();
        chauffeurDto.setId(5L);
        chauffeurDto.setName("장화평");
        chauffeurDto.setCellPhone("010-4104-7721");
        return chauffeurDto;
    }

}


