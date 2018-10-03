package com.worldgether.mbal.controller;

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

    @RequestMapping("/oauth/confirm_access_mbal")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request, Principal principal) throws Exception {
        ModelAndView modelAndView = new ModelAndView();

        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        String clientId = authorizationRequest.getClientId();
        modelAndView.addObject("clientId", clientId);

        modelAndView.addObject("nameUser", principal.getName());

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
