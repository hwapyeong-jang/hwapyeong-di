package com.macaront.framework.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macaront.framework.web.servlet.handler.HandlerAdapter;
import com.macaront.framework.web.servlet.handler.HandlerMapping;
import com.macaront.framework.web.servlet.ioc.IocContainer;
import com.macaront.framework.web.servlet.mv.ModelAndView;
import com.macaront.framework.web.servlet.mv.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class DispatcherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    protected DispatcherServlet(String rootPackage) {
        // IOC Container 생성
        IocContainer.create(rootPackage);

        // RequestMapping Handler 생성
        HandlerMapping.create();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        ModelAndView modelAndView = (ModelAndView) HandlerAdapter.invoke(req);

        if (isNotExistRequestMapping(modelAndView)) {
            // app.js, style.css 등 Controller 에서 Mapping 정보를 찾을 수 없는 경우
            fileStreamProcess(req.getRequestURI(), resp);
        } else {
            // Controller 에 Mapping 정보가 존재하는 경우
            modelAndViewProcess(modelAndView, resp);
        }
    }

    private boolean isNotExistRequestMapping(ModelAndView modelAndView) {
        return modelAndView == null;
    }

    private void modelAndViewProcess(ModelAndView modelAndView, HttpServletResponse resp) {
        if (modelAndView.getModelMap() != null) {
            dataStreamProcess(modelAndView, resp);
        }

        if (modelAndView.getView() != null) {
            fileStreamProcess(modelAndView.getView(), resp);
        }
    }

    private void dataStreamProcess(ModelAndView modelAndView, HttpServletResponse resp) {
        resp.setContentType("application/json");
        try {
            String jsonString = new ObjectMapper().writeValueAsString(modelAndView.getModelMap().get("data"));
            resp.getWriter().write(jsonString);
            logger.info("Json Data OutputStream: {}", jsonString);
        } catch (IOException e) {
            logger.info("Json Data OutputStream Error");
        }
    }

    private void fileStreamProcess(String path, HttpServletResponse resp) {
        String fileName = ViewResolver.getView(path);
        File file = new File(fileName);
        if (file.exists()) {
            try (OutputStream os = resp.getOutputStream()) {
                Files.copy(file.toPath(), os);
                logger.info("File OutputStream: {}", fileName);
            } catch (IOException e) {
                logger.error("File OutputStream Error: {}", fileName);
            }
        }
    }

}
