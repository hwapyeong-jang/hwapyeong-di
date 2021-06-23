package com.macaront.app.api.controller;

import com.macaront.framework.web.servlet.mv.ModelAndView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ChauffeurViewControllerTest {

    private static ChauffeurViewController chauffeurViewController;

    @BeforeAll
    public static void initController() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ChauffeurViewController> constructor = ChauffeurViewController.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        chauffeurViewController = constructor.newInstance();
    }

    @Test
    public void testIndex() {

        // given
        String expectedPage = "index.html";

        //when
        ModelAndView response = chauffeurViewController.index();

        // then
        Assertions.assertEquals(expectedPage, response.getView());

    }

}
