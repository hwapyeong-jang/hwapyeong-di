package com.macaront.app.api.service;

import com.macaront.app.api.param.ChauffeurParam;
import com.macaront.app.domain.dto.ChauffeurDto;
import com.macaront.app.domain.service.ChauffeurService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ChauffeurApiServiceTest {

    private static ChauffeurApiService chauffeurApiService;

    private static ChauffeurService.Mock chauffeurServiceMock;

    @BeforeAll
    public static void injectMock() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ChauffeurService.Mock> chauffeurServiceMockConstructor =
                ChauffeurService.Mock.class.getDeclaredConstructor();
        chauffeurServiceMockConstructor.setAccessible(true);
        chauffeurServiceMock = chauffeurServiceMockConstructor.newInstance();

        Constructor<ChauffeurApiService> apiServiceConstructor =
                ChauffeurApiService.class.getDeclaredConstructor(ChauffeurService.class);
        apiServiceConstructor.setAccessible(true);
        chauffeurApiService = apiServiceConstructor.newInstance(chauffeurServiceMock);
    }

    @Test
    public void testGetList_exist_case() {
        testGetList(getChauffeurDtoList());
    }

    @Test
    public void testGetList_not_exist_case() {
        testGetList(new ArrayList<>());
    }

    private void testGetList(List<ChauffeurDto> givenList) {

        // given
        chauffeurServiceMock.chauffeurDtoList = givenList;

        // when
        List<ChauffeurDto> resList = chauffeurApiService.getList();

        // then
        Assertions.assertEquals(givenList.size(), resList.size());

    }

    @Test
    public void testGetData_exist_case() {

        // given
        Long givenId = 1L;
        chauffeurServiceMock.chauffeurDto = getChauffeurDto();

        // when
        ChauffeurDto resChauffeurDto = chauffeurApiService.getData(givenId);

        // then
        Assertions.assertEquals(givenId, resChauffeurDto.getId());
        Assertions.assertEquals(chauffeurServiceMock.chauffeurDto.getName(), resChauffeurDto.getName());
        Assertions.assertEquals(chauffeurServiceMock.chauffeurDto.getCellPhone(), resChauffeurDto.getCellPhone());

    }

    @Test
    public void testGetData_not_exist_case() {

        // given
        Long givenId = 1L;
        chauffeurServiceMock.chauffeurDto = null;

        // when
        ChauffeurDto resChauffeurDto = chauffeurApiService.getData(givenId);

        // then
        Assertions.assertNull(resChauffeurDto);

    }

    @Test
    public void testAdd() {

        // given
        ChauffeurParam givenChauffeurParam = getChauffeurParam();
        chauffeurServiceMock.chauffeurDto = givenChauffeurParam.convertToChauffeurDto();

        // when
        ChauffeurDto resChauffeurDto = chauffeurApiService.add(givenChauffeurParam);

        // then
        Assertions.assertTrue(resChauffeurDto.isEqualsValues(givenChauffeurParam));
        Assertions.assertEquals(chauffeurServiceMock.chauffeurDto.getName(), resChauffeurDto.getName());
        Assertions.assertEquals(chauffeurServiceMock.chauffeurDto.getCellPhone(), resChauffeurDto.getCellPhone());

    }

    @Test
    public void testAdd_not_exist_name() {

        // given
        ChauffeurParam givenChauffeurParam = getChauffeurParam();
        givenChauffeurParam.setName(null);

        // when
        testAdd_not_exist_param(givenChauffeurParam);
    }

    @Test
    public void testAdd_not_exist_cellPhone() {

        // given
        ChauffeurParam givenChauffeurParam = getChauffeurParam();
        givenChauffeurParam.setCellPhone(null);

        // when
        testAdd_not_exist_param(givenChauffeurParam);
    }

    private void testAdd_not_exist_param(ChauffeurParam givenChauffeurParam) {

        // given
        chauffeurServiceMock.chauffeurDto = givenChauffeurParam.convertToChauffeurDto();

        // when
        chauffeurApiService.add(givenChauffeurParam);

    }

    @Test
    public void testModify() {

        // given
        Long givenId = null;
        chauffeurServiceMock.chauffeurDto = getChauffeurDto();
        ChauffeurParam givenChauffeurParam = getChauffeurParam();

        // when
        chauffeurApiService.modify(givenId, givenChauffeurParam);

    }

    @Test
    public void testModify_not_exist_id() {

        // given
        Long givenId = 1L;
        chauffeurServiceMock.chauffeurDto = getChauffeurDto();
        ChauffeurParam givenChauffeurParam = getChauffeurParam();

        // when
        ChauffeurDto resChauffeurDto = chauffeurApiService.modify(givenId, givenChauffeurParam);

        // then
        Assertions.assertEquals(givenId, resChauffeurDto.getId());
        Assertions.assertTrue(resChauffeurDto.isEqualsValues(givenChauffeurParam));

    }

    @Test
    public void testDelete() {

        // given
        Long givenId = 1L;

        // when
        chauffeurApiService.delete(givenId);

    }

    private List<ChauffeurDto> getChauffeurDtoList() {
        List<ChauffeurDto> list = new ArrayList<>();
        list.add(getChauffeurDto());
        return list;
    }

    private ChauffeurDto getChauffeurDto() {
        ChauffeurDto dto = new ChauffeurDto();
        dto.setId(1L);
        dto.setName("장화평");
        dto.setCellPhone("010-4104-7721");
        return dto;
    }

    private ChauffeurParam getChauffeurParam() {
        ChauffeurParam param = new ChauffeurParam();
        param.setName("장화평");
        param.setCellPhone("010-4104-7721");
        return param;
    }

}
