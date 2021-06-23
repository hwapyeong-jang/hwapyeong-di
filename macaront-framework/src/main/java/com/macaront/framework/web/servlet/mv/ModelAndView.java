package com.macaront.framework.web.servlet.mv;

public class ModelAndView {

    private String view;

    private ModelMap modelMap;

    public ModelAndView(String view) {
        this.view = view;
    }

    public ModelAndView(ModelMap modelMap) {
        this.modelMap = modelMap;
    }

    public String getView() {
        return this.view;
    }

    public ModelMap getModelMap() {
        return this.modelMap;
    }

}
