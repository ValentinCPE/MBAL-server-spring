package com.worldgether.mbal.controller;

import com.worldgether.mbal.model.LoggerMessage;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@Controller
@SessionAttributes("authorizationRequest")
public class ConfirmAccessMbalController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment environment;

    @Autowired
    private UserService userService;

    @RequestMapping("/oauth/confirm_access_mbal")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request, Principal principal) throws Exception {
        ModelAndView modelAndView = new ModelAndView();

        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");

        String clientId;
        if(authorizationRequest != null) clientId = authorizationRequest.getClientId();
        else clientId = "inconnu";
        modelAndView.addObject("clientId", clientId.substring(0,1).toUpperCase() + clientId.substring(1).toLowerCase());

        String startPathProfilePicture = "";

        if(this.environment.getActiveProfiles()[0].equals("production")){
            startPathProfilePicture = "/MBAL";
            modelAndView.addObject("pathBackgroundImage", "/MBAL/resources/login/images/bg-01.jpg");
        }else{
            modelAndView.addObject("pathBackgroundImage", "/resources/login/images/bg-01.jpg");
        }

        User user = null;
        if(principal != null) user = userService.getUser(principal.getName());
        if(user != null){
            modelAndView.addObject("nameUser", user.getPrenom().substring(0,1).toUpperCase()+user.getPrenom().substring(1).toLowerCase()+" "+user.getNom().toUpperCase());

            if(user.getProfile_picture_path() != null && !user.getProfile_picture_path().isEmpty()){
                modelAndView.addObject("pathImageProfile", startPathProfilePicture + "/public/files/" + user.getProfile_picture_path());
            }else{
                log.error(LoggerMessage.getLog(LoggerMessage.IMAGE_DEFAULT_NO_PROFILE_PICTURE.toString(),"ConfirmAccessMbalController", "bonhomme.jpg"));
                modelAndView.addObject("pathImageProfile", startPathProfilePicture + "/public/files/bonhomme.jpg");
            }
        }else{
            modelAndView.addObject("nameUser", "inconnu".substring(0,1).toUpperCase()+"inconnu".substring(1).toLowerCase());
            modelAndView.addObject("pathImageProfile", startPathProfilePicture + "/public/files/bonhomme.jpg");
        }

        if (request.getAttribute("_csrf") != null) {
            model.put("_csrf", request.getAttribute("_csrf"));
        }

        CsrfToken csrfToken = (CsrfToken) (model.containsKey("_csrf") ? model.get("_csrf") : request.getAttribute("_csrf"));

        if(csrfToken != null) {
            modelAndView.addObject("csrfTokenParameter", csrfToken.getParameterName());
            modelAndView.addObject("csrfTokenGetToken", csrfToken.getToken());
        }

        modelAndView.setViewName("confirm_access");

        return modelAndView;
    }

}
