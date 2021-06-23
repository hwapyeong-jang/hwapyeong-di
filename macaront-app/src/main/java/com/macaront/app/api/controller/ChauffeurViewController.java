package com.macaront.app.api.controller;

import com.macaront.framework.web.annotation.Controller;
import com.macaront.framework.web.annotation.RequestMapping;
import com.macaront.framework.web.annotation.RequestMethod;
import com.macaront.framework.web.servlet.mv.ModelAndView;

@Controller
public class ChauffeurViewController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }

}
