package com.macaront.app.api.controller;

import com.macaront.app.api.param.ChauffeurParam;
import com.macaront.app.api.service.ChauffeurApiService;
import com.macaront.app.domain.dto.ChauffeurDto;
import com.macaront.framework.web.servlet.mv.ModelAndView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ChauffeurApiControllerTest {

    private static ChauffeurApiController chauffeurApiController;

    private static ChauffeurApiService.Mock chauffeurApiServiceMock;

    @BeforeAll
    public static void injectMock() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ChauffeurApiService.Mock> chauffeurApiServiceConstructor = ChauffeurApiService.Mock.class.getDeclaredConstructor();
        chauffeurApiServiceConstructor.setAccessible(true);
        chauffeurApiServiceMock = chauffeurApiServiceConstructor.newInstance();

        Constructor<ChauffeurApiController> apiControllerConstructor =
                ChauffeurApiController.class.getDeclaredConstructor(ChauffeurApiService.class);
        apiControllerConstructor.setAccessible(true);
        chauffeurApiController = apiControllerConstructor.newInstance(chauffeurApiServiceMock);
    }

    @Test
    public void testGetList() {

        // given
        chauffeurApiServiceMock.chauffeurDtoList = getChauffeurDtoList();

        // when
        ModelAndView modelAndView = chauffeurApiController.getList();

        // then
        Assertions.assertEquals(chauffeurApiServiceMock.chauffeurDtoList, modelAndView.getModelMap().get("data"));

    }

    @Test
    public void testGetData() {

        // given
        Long givenId = 1L;
        chauffeurApiServiceMock.chauffeurDto = getChauffeurDto();

        // when
        ModelAndView modelAndView = chauffeurApiController.getData(givenId);

        // then
        Assertions.assertEquals(chauffeurApiServiceMock.chauffeurDto, modelAndView.getModelMap().get("data"));

    }

    @Test
    public void testAdd() {

        // given
        ChauffeurParam givenParam = getChauffeurParam();
        chauffeurApiServiceMock.chauffeurDto = getChauffeurDto();

        // when
        ModelAndView modelAndView = chauffeurApiController.add(givenParam);

        // then
        Assertions.assertEquals(chauffeurApiServiceMock.chauffeurDto, modelAndView.getModelMap().get("data"));

    }

    @Test
    public void testModify() {

        // given
        Long givenId = 1L;
        ChauffeurParam givenParam = getChauffeurParam();
        chauffeurApiServiceMock.chauffeurDto = getChauffeurDto();

        // when
        ModelAndView modelAndView = chauffeurApiController.modify(givenId, givenParam);

        // then
        Assertions.assertEquals(chauffeurApiServiceMock.chauffeurDto, modelAndView.getModelMap().get("data"));

    }

    @Test
    public void testDelete() {

        // given
        Long givenId = 1L;

        // when
        chauffeurApiController.delete(givenId);

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
        ChauffeurParam chauffeurParam = new ChauffeurParam();
        chauffeurParam.setName("장화평");
        chauffeurParam.setCellPhone("010-4104-7721");
        return chauffeurParam;
    }

}
