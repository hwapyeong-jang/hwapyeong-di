package com.macaront.app.api.controller;

import com.macaront.app.api.param.ChauffeurParam;
import com.macaront.app.api.service.ChauffeurApiService;
import com.macaront.framework.web.annotation.*;
import com.macaront.framework.web.servlet.mv.ModelAndView;
import com.macaront.framework.web.servlet.mv.ModelMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(value = "/chauffeurs")
public class ChauffeurApiController {

    private static final Logger logger = LoggerFactory.getLogger(ChauffeurApiController.class);

    private final ChauffeurApiService chauffeurApiService;

    private ChauffeurApiController(ChauffeurApiService chauffeurApiService) {
        this.chauffeurApiService = chauffeurApiService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getList() {
        logger.info("Chauffeur API Controller Layer - Get Chauffeur List");
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("data", chauffeurApiService.getList());
        return new ModelAndView(modelMap);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView getData(@PathVariable Long id) {
        logger.info("Chauffeur API Controller Layer - Get Chauffeur Data, id: {}", id);
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("data", chauffeurApiService.getData(id));
        return new ModelAndView(modelMap);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView add(@RequestBody ChauffeurParam param) {
        logger.info("Chauffeur API Controller Layer - Add Chauffeur, name: {}, cellPhone: {}",
                param.getName(), param.getCellPhone()
        );
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("data", chauffeurApiService.add(param));
        return new ModelAndView(modelMap);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ModelAndView modify(@PathVariable Long id, @RequestBody ChauffeurParam param) {
        logger.info("Chauffeur API Controller Layer - Modify Chauffeur, id: {}, name: {}, cellPhone: {}",
                id, param.getName(), param.getCellPhone()
        );
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("data", chauffeurApiService.modify(id, param));
        return new ModelAndView(modelMap);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        logger.info("Chauffeur API Controller Layer - Delete Chauffeur, id: {}", id);
        chauffeurApiService.delete(id);
    }

}
